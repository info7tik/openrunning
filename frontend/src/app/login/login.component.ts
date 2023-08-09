import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { TokenManagerService } from '../token-manager.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    email: string = "";
    password: string = "";
    errorMessage = 'Invalid Credentials';
    successMessage: string = "";
    invalidLogin = false;
    loginSuccess = false;

    constructor(private router: Router, private authService: AuthService, private tokenManager: TokenManagerService) { }

    ngOnInit(): void {
        if (this.tokenManager.getToken().length > 0) {
            this.router.navigate(['/home']);
        }
      }


    handleLogin() {
        this.authService.signin(this.email, this.password).subscribe({
            next: (data) => {
                this.tokenManager.saveToken(this.email, data.token);
                this.invalidLogin = false;
                this.loginSuccess = true;
                this.successMessage = 'Login Successful.';
                this.router.navigate(['/home']);
                },
            error: () => {
                this.invalidLogin = true;
                this.loginSuccess = false;
                }
        });
    }
}
