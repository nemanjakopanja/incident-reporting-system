import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import { LoginResponse } from '../../model/login-response';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { LoginRequest } from '../../model/login-request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup;
  loginErrorMessage: string = '';

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required]]
    });
  }


  login() {
    
    const loginRequest: LoginRequest = {
      username: this.loginForm.get('username')?.value,
      password: this.loginForm.get('password')?.value
    }

    this.userService.login(loginRequest).subscribe(
      (data: LoginResponse) => {
        console.log("successful login data: ", data);
        this.loginErrorMessage = '';
        localStorage.setItem("IRA_currentUser", JSON.stringify(data.user));
        localStorage.setItem("IRA_currentUser_role", data.user.role);
        localStorage.setItem("IRA_currentUser_token", data.token);
        this.router.navigate(['/home']);
      },
      (error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.loginErrorMessage = error.error.message;
        } else {
          this.loginErrorMessage = 'Greška na serveru. Pokušajte kasnije.';
        }
      }
    )
  }

  register() {
    this.router.navigate(['/register']);
  }
}
