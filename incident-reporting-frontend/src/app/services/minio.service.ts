import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MinioService extends BaseService {

  minioUrl = this.baseUrl + "/api/storage";
  
  constructor(private http: HttpClient) {
    super();
  }

  uploadFile(file: File): Observable<string> {
    const uploadPictureUrl = this.minioUrl + "/upload";

    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(uploadPictureUrl, formData, { responseType: 'text' }).pipe(
      catchError((error) => {
        console.log("error uploading picture");
        return throwError(() => error);
      })
    );
  }

  getPresignedUrl(fileName: string): Observable<string> {
	console.log('metoda getPresignedUrl');
    const getPresignedUrl = this.minioUrl + `/presigned/${fileName}`;
    return this.http.get(getPresignedUrl, { responseType: 'text' }).pipe(
      catchError((error) => {
        console.log("error getting presignedUrl");
        return throwError(() => error);
      })
    );
  }
}
