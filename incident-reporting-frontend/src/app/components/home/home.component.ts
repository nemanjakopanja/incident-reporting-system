import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Incident } from '../../model/incident';
import { MinioService } from '../../services/minio.service';
import { IncidentService } from '../../services/incident.service';
import { IncidentRequest } from '../../model/incident-request';
import { MatTableDataSource } from '@angular/material/table';
import { ModeratorService } from '../../services/moderator.service';
import { ModeratorAction } from '../../model/moderator-action';
import { User } from '../../model/user';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  role!: string;
  currentUser!: User;
  incidentForm: FormGroup;
  selectedFile!: File;
  incidentReportedMessage: string = '';

  incidents: Incident[] = [];
  dataSource!: any;
  displayedColumns: string[] = ['id', 'type', 'date', 'description', 'latitude', 'longitude', 'image', 'status', 'approve', 'reject'];
  
  incidentBucketUrl = 'http://localhost:9000/incident-bucket/';

  constructor(private router: Router, private formBuilder: FormBuilder, private minioService: MinioService, private incidentService: IncidentService, private moderatorService: ModeratorService) {
    this.incidentForm = this.formBuilder.group({
        type: ['', Validators.required],
        longitude: ['', Validators.required],
        latitude: ['', Validators.required],
        description: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.getAllIncidents();
    this.incidentReportedMessage = '';
    this.role = localStorage.getItem('IRA_currentUser_role') || '';
    const userJson = localStorage.getItem('IRA_currentUser');
    if (userJson) {
      this.currentUser = JSON.parse(userJson) as User;
    }
  }

  /*getAllIncidents() {
    this.incidentService.getAllIncidents().subscribe(
      (data: Incident[]) => {
        this.incidents = data;
        this.dataSource = new MatTableDataSource(this.incidents);
        this.incidents.forEach(i => console.log('imgUrl: ', i.imageUrl));
      },
      (error) => {
        console.log('error while getting all incidents');
      }
    );
  }*/

  getAllIncidents() {
    this.incidentService.getAllIncidents().subscribe(
      (data: Incident[]) => {
        this.incidents = data;

        // filtriramo incidente sa slikom
        const incidentsWithImage = this.incidents.filter(i => i.imageUrl && i.imageUrl !== 'N/A');

        // ako nema incidenata sa slikom, odmah postavi dataSource i izađi
        if (incidentsWithImage.length === 0) {
          this.dataSource = new MatTableDataSource(this.incidents);
          return;
        }
		
		incidentsWithImage.forEach(i => {
          i.imageUrl = this.incidentBucketUrl + i.imageUrl;
        });

        this.dataSource = new MatTableDataSource(this.incidents);

        // mapiramo na observables za dohvat presigned URL-a
        /*const observables = incidentsWithImage.map(i =>
          this.minioService.getPresignedUrl(i.imageUrl)
        );

        forkJoin(observables).subscribe(
          (urls: string[]) => {
            // postavimo prave URL-ove u incidente
            urls.forEach((url, index) => {
              incidentsWithImage[index].imageUrl = url;
            });

            // sada postavimo dataSource sa ažuriranim URL-ovima
            this.dataSource = new MatTableDataSource(this.incidents);

            // debug
            this.incidents.forEach(i => console.log('imgUrl: ', i.imageUrl));
          },
          error => {
            console.log('Error fetching presigned URLs', error);
            // čak i u slučaju greške postavimo dataSource sa originalnim URL-ovima
            this.dataSource = new MatTableDataSource(this.incidents);
          }
        );*/
      },
      (error) => {
        console.log('error while getting all incidents');
      }
    );
  }

  approve(incident: Incident) {
    console.log('approve method');

    incident.status = 'APPROVED';
    this.updateIncident(incident);
  }

  reject(incident: Incident) {
    console.log('recejt method');

    incident.status = 'REJECTED';
    this.updateIncident(incident);
  }

  updateIncident(incident: Incident) {
    const moderatorAction: ModeratorAction = {
      incident: incident,
      moderatorUsername: this.currentUser.username 
    };

    this.moderatorService.updateIncident(moderatorAction).subscribe(
      (data: void) => {
        console.log('updated incident');
        this.getAllIncidents();
      },
      (error) => {
        console.log('error updating incident');
      }
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    console.log(filterValue);
    this.dataSource.filter = filterValue;

    console.log('apply filter');
  }

  onMapClick(coords: { latitude: number; longitude: number }) {
    this.incidentForm.patchValue({
      latitude: coords.latitude,
      longitude: coords.longitude
    });
  }

  logout() {
    console.log('logout');
    localStorage.removeItem('IRA_currentUser');
    localStorage.removeItem('IRA_currentUser_role');
    localStorage.clear();
    this.router.navigate(['/public']);
  }

  onFileSelected(event: Event): void {
  const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      console.log('Selected file:', this.selectedFile);
    }
  }

  reportNewIncident() {
    if (this.selectedFile) {
      this.minioService.uploadFile(this.selectedFile).subscribe({
        next: (minioImageUrl: string) => {
          const incident: IncidentRequest = {
            type: this.incidentForm.value.type,
            longitude: this.incidentForm.value.longitude,
            latitude: this.incidentForm.value.latitude,
            description: this.incidentForm.value.description,
            imageUrl: minioImageUrl
          };

          this.incidentService.reportNewIncident(incident).subscribe({
            next: res => {
              console.log('Incident created', res);
              this.incidentReportedMessage = 'Uspješna prijava!';
            },
            error: err => console.error('Error creating incident', err)
          });
        },
        error: err => {
          console.error('Upload failed', err);
          alert('Greška prilikom uploada slike');
        }
      });
    } else {
      // Ako nema fajla
      const incident: IncidentRequest = {
        type: this.incidentForm.value.type,
        longitude: this.incidentForm.value.longitude,
        latitude: this.incidentForm.value.latitude,
        description: this.incidentForm.value.description,
        imageUrl: 'N/A'
      };

      this.incidentService.reportNewIncident(incident).subscribe({
        next: res => {
              console.log('Incident created', res);
              this.incidentReportedMessage = 'Uspješna prijava!';
            },
        error: err => console.error('Error creating incident', err)
      });
    }
  }

  /*reportNewIncident() {
    console.log('new incident method');
    let minioImageUrl: string = 'N/A';

    if (this.selectedFile) {
      this.minioService.uploadFile(this.selectedFile).subscribe(
        (data: string) => {
          minioImageUrl = data;
          console.log('minio imageUrl: ', data);
        },
        (error) => {
          console.log('upload picture by minio error');
        }
      );
    }

    setTimeout(() => {
      
      const incident: Incident = {
        id: -1,
        type: this.incidentForm.get('type')?.value,
        longitude: this.incidentForm.get('longitude')?.value,
        latitude: this.incidentForm.get('latitude')?.value,
        description: this.incidentForm.get('description')?.value,
        imageUrl: minioImageUrl,
        status: '',
        createdAt: new Date()
      };

      console.log('new incident: ', incident);
    }, 1000);
  }*/
}
