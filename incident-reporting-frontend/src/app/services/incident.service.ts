import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Incident } from '../model/incident';
import { IncidentRequest } from '../model/incident-request';

@Injectable({
  providedIn: 'root'
})
export class IncidentService extends BaseService {

  incidentsUrl = this.baseUrl + "/api/incidents";

  constructor(private http: HttpClient) {
    super();
  }

  getAllIncidents(): Observable<Incident[]> {
    return this.http.get<Incident[]>(this.incidentsUrl).pipe(
      catchError((error) => {
        console.log("error getting incidents");
        return throwError(() => error);
      })
    );
  }

  reportNewIncident(incident: IncidentRequest): Observable<void> {
    const reportNewIncidentUrl = this.incidentsUrl + '/report'
    return this.http.post<void>(reportNewIncidentUrl, incident).pipe(
      catchError((error) => {
        console.log("error reporting incidents");
        return throwError(() => error);
      })
    );
  }
}
