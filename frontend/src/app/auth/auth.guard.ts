import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenManagerService } from '../token-manager.service';

export const authGuard: CanActivateFn = (route, state) => {
    const tokenManager = inject(TokenManagerService);
    if (tokenManager.getToken().length > 0) {
        return true;
    } else {
        const router = inject(Router);
        router.navigate(['/login']);
        return false;
    }
};
