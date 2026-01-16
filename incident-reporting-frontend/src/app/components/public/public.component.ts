import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Incident } from '../../model/incident';
import { IncidentService } from '../../services/incident.service';

@Component({
  selector: 'app-public',
  templateUrl: './public.component.html',
  styleUrl: './public.component.css'
})
export class PublicComponent {

  constructor(private router: Router) {
  
  }

  login() {
    this.router.navigate(['/login']);
  }

  register() {
    this.router.navigate(['/register']);
  }
}
