import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    localStorage.removeItem('IRA_currentUser');
    localStorage.removeItem('IRA_currentUser_role');
    router.navigate(['/public']);
    return false;
  }

  const userRole = authService.getUserRole();
  const allowedRoles = route.data?.['roles'] as string[];

  if (allowedRoles && !allowedRoles.includes(userRole)) {
    router.navigate(['/home']);
    return false;
  }

  return true;
};
