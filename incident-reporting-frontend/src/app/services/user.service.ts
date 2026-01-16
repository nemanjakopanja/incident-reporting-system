import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { User } from '../model/user';
import { LoginResponse } from '../model/login-response';
import { LoginRequest } from '../model/login-request';
import { RegisterRequest } from '../model/register-request';
import { RegisterResponse } from '../model/register-response';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseService {

  loginUrl: string = this.baseUrl + '/api/auth/login';
  registerUrl: string = this.baseUrl + '/api/auth/register';

  constructor(private http: HttpClient) {
    super();
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    console.log('login credentials:', loginRequest);
    return this.http.post<LoginResponse>(this.loginUrl, loginRequest).pipe(
      catchError(error => {
        return throwError(() => error);
      })
    );
  }

  register(registerRequest: RegisterRequest): Observable<boolean> {
    console.log('register credentials:', registerRequest);
    return this.http.post<boolean>(this.registerUrl, registerRequest).pipe(
      catchError(error => {
        return throwError(() => error);
      })
    );
  }
}
