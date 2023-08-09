import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})
export class SignupComponent {
    email: string = "";
    password: string = "";
    errorMessage = 'Invalid Credentials';
    signupMessage: string = "";
    invalidLogin = false;
    loginSuccess = false;

    constructor(private router: Router, private authService: AuthService) { }

    handleSignup() {
        this.authService.signup(this.email, this.password).subscribe({
            next: () => {
                this.signupMessage = 'Signup Successful.';
                this.router.navigate(['/home']);
            },
            error: (data) => {
                this.signupMessage = data.error.message;
            }
        });
    }
}
