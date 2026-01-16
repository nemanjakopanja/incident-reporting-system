import { AfterViewInit, Component, ElementRef, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import L from 'leaflet';
import { Incident } from '../../model/incident';
import { IncidentService } from '../../services/incident.service';
import { MinioService } from '../../services/minio.service';
import { forkJoin } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements AfterViewInit, OnInit {

  allIncidents!: Incident[];
  approvedIncidents: Incident[] = [];
  filterForm: FormGroup;
  filteredIncidents: Incident[] = [];
  
  incidentBucketUrl = 'http://localhost:9000/incident-bucket/';

  @Output()
  mapClick = new EventEmitter<{ latitude: number; longitude: number }>();

  @ViewChild('mapContainer', { static: false })
  mapContainer!: ElementRef<HTMLDivElement>;

  map!: L.Map;
  markersLayer!: L.LayerGroup;
  markers: L.Marker[] = [];

  constructor(private incidentService: IncidentService, private minioService: MinioService, private formBuilder: FormBuilder) {
    this.filterForm = this.formBuilder.group({
      type: ['', Validators.required],
      time: ['', Validators.required]
    });
  }
  ngOnInit(): void {
    this.getAllIncidents();
  }

  getAllIncidents(): void {
    this.incidentService.getAllIncidents().subscribe(
      (data: Incident[]) => {
        this.allIncidents = data;

        this.approvedIncidents = this.allIncidents.filter(
          i => i.status === 'APPROVED'
        );

        // prvo prikažem incidente bez slike na mapi
        this.approvedIncidents.forEach(i => {
          if (!i.imageUrl || i.imageUrl === 'N/A')
            this.addIncidentMarker(i);
        });

        // samo za incidente sa slikom tražimo presigned URL
        const incidentsWithImage = this.approvedIncidents.filter(
          i => i.imageUrl && i.imageUrl !== 'N/A'
        );
		
		incidentsWithImage.forEach(i => {
          i.imageUrl = this.incidentBucketUrl + i.imageUrl;
          this.addIncidentMarker(i);
        });
		
		

        /*const observables = incidentsWithImage.map(i =>
          this.minioService.getPresignedUrl(i.imageUrl)
        );

        if (observables.length === 0) {
          return;
        }
		
		console.log('observables length:', observables.length);
		observables.forEach((obs, idx) => console.log('observable', idx, obs));

        forkJoin(observables).subscribe(
          (urls: string[]) => {
            urls.forEach((url, index) => {
			
			  console.log('presigned url: ', url);
			
			  if (url.startsWith('http://minio')) {
                let new_url = url.replace('http://minio', 'http://localhost');
				console.log('new presigned url: ', new_url);
				incidentsWithImage[index].imageUrl = new_url;
			  }
				
              incidentsWithImage[index].imageUrl = url;

              // sad prikazujem incidente sa slikom
              this.addIncidentMarker(incidentsWithImage[index]);
            });
          },
          error => console.log('Error fetching presigned URLs', error)
        );*/
      },
      error => console.log('error fetching incidents')
    );
  }

  addIncidentMarker(i: Incident) {
    const date = new Date(i.createdAt);
    const formattedDate =
      `${('0'+date.getDate()).slice(-2)}.${('0'+(date.getMonth()+1)).slice(-2)}.${date.getFullYear()} ` +
      `${('0'+date.getHours()).slice(-2)}:${('0'+date.getMinutes()).slice(-2)}`;

    const imageHtml = i.imageUrl && i.imageUrl !== 'N/A' ? `<img style="height:150px; width:auto;" src="${i.imageUrl}">` : ``;

    const popupContent = `
      <div style="text-align:center; min-width:250px;">
        <b>${i.type}</b><br><br>
        ${i.description}<br>
        ${formattedDate}<br>
        ${imageHtml}
      </div>
    `;

    this.addMarker(i.latitude, i.longitude, popupContent);
  }

  ngAfterViewInit(): void {

    // Fix za marker
    delete (L.Icon.Default.prototype as any)._getIconUrl;
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: '../assets/marker-icon-2x.png',
      iconUrl: '../assets/marker-icon.png',
      shadowUrl: '../assets/marker-shadow.png'
    });

    this.map = L.map(this.mapContainer.nativeElement);
    this.markersLayer = L.layerGroup().addTo(this.map);

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const lat = position.coords.latitude;
          const lng = position.coords.longitude;

          this.setView(lat, lng, 14);
        },
        (error) => {
          console.error('Geolocation error', error);
          this.fallbackLocation();
        }
      );
    } else {
      this.fallbackLocation();
    }

    if (localStorage.getItem('IRA_currentUser')) {
      // click listener
      this.map.on('click', (e: L.LeafletMouseEvent) => {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        this.mapClick.emit({
          latitude: lat,
          longitude: lng
        });
      });
    }
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.off();
      this.map.remove();
    }
  }

  fallbackLocation() {
    // ako korisnik odbije lokaciju
    this.setView(44.7722, 17.1910, 14);
  }

  setView(lat: number, lng: number, zoom: number) {
    this.map.setView([lat, lng], zoom);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(this.map);
  }

  addMarker(lat: number, lng: number, text: string) {
    L.marker([lat, lng]).bindPopup(text).addTo(this.markersLayer);
  }

  clearMarkers() {
    this.markersLayer.clearLayers();
  }

  filterIncidents() {
    console.log('filter incidents');

    let type = this.filterForm.get('type')?.value;
    let time = this.filterForm.get('time')?.value;

    console.log('type: ', type);
    console.log('time: ', time);

    this.clearMarkers();

    if (type === 'allIncidents' && time === 'allIncidents') {
      this.filteredIncidents = this.approvedIncidents;
    } else if (type === 'allIncidents' && time !== 'allIncidents') {
      this.filteredIncidents = this.approvedIncidents.filter(i => {
        let currentTime = new Date().getTime();
        let selectedMs = currentTime - (parseInt(time) * 24 * 60 * 60 * 1000);
        let incidentMs = new Date(i.createdAt).getTime();

        return incidentMs > selectedMs;
      });
    } else if (time === 'allIncidents' && type !== 'allIncidents') {
      this.filteredIncidents = this.approvedIncidents.filter(i => i.type === type);
    } else {
      this.filteredIncidents = this.approvedIncidents.filter(i => {
        let currentTime = new Date().getTime();
        let selectedMs = currentTime - (parseInt(time) * 24 * 60 * 60 * 1000);
        let incidentMs = new Date(i.createdAt).getTime();

        return incidentMs > selectedMs && i.type === type;
      });
    }

    this.filteredIncidents.forEach(i => this.addIncidentMarker(i));

    console.log('kraj metode');
  }
}
