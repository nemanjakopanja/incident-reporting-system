import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RegisterRequest } from '../../model/register-request';
import { RegisterResponse } from '../../model/register-response';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  
  registerForm: FormGroup;
  registrationErrorMessage!: string;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService) {
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  register() {
    console.log(this.registerForm.value);

    const userRequest: RegisterRequest = {
      firstName: this.registerForm.get('firstName')?.value,
      lastName: this.registerForm.get('lastName')?.value,
      username: this.registerForm.get('username')?.value,
      password: this.registerForm.get('password')?.value
    };
    //console.log(userRequest.firstName);
    //console.log(userRequest.lastName);
    //console.log(userRequest.username);

    this.userService.register(userRequest).subscribe(
      (response: boolean) => {
        console.log('response: ', response);
        console.log('Uspjesna registracija BRALE');
        console.log('*** ', response, ' ***');
        this.registrationErrorMessage = 'Uspješna registracija!';
        //alert("Uspješna registracija! Provjerite Vaš mail zbog aktivacionog linka.");
      },
      (error: HttpErrorResponse) => {
        console.log('error status', error.status);
        console.log('error statusText', error.statusText);
        console.log('Neuspješna registracija!');
        this.registrationErrorMessage = 'Korisničko ime zauzeto!';
        //alert(error.error.error);
      }
    );
  }

  login() {
    this.router.navigate(['/login']);
  }
}
