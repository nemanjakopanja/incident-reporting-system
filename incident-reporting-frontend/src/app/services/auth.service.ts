import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('IRA_currentUser');
  }

  getUserRole(): string {
    return localStorage.getItem('IRA_currentUser_role') || '';
  }
}
