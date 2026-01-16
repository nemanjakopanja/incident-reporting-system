import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { Incident } from '../model/incident';
import { catchError, Observable, throwError } from 'rxjs';
import { ModeratorAction } from '../model/moderator-action';

@Injectable({
  providedIn: 'root'
})
export class ModeratorService extends BaseService {

  moderatorUrl = this.baseUrl + "/api/moderator";

  constructor(private http: HttpClient) { 
    super();
  }

  updateIncident(moderatorAction: ModeratorAction): Observable<void> {
    const updateIncidentUrl = this.moderatorUrl + `/update/${moderatorAction.incident.id}`;
    return this.http.put<void>(updateIncidentUrl, moderatorAction).pipe(
      catchError((error) => {
        console.log("error updating incident");
        return throwError(() => error);
      })
    );
  }

}
