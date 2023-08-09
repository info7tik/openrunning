import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class TokenManagerService {
    constructor() { }

    TOKEN_KEY: string = "auth-token";
    EMAIL_KEY: string = "auth-user";

    public saveToken(email: string, token: string) {
        localStorage.setItem(this.EMAIL_KEY, email);
        localStorage.setItem(this.TOKEN_KEY, token);
    }

    public deleteToken() {
        localStorage.removeItem(this.EMAIL_KEY);
        localStorage.removeItem(this.TOKEN_KEY);
    }

    public getEmail(): string {
        let email = localStorage.getItem(this.EMAIL_KEY);
        if (email !== null) {
            return email;
        } else {
            return ""
        }
    }

    public getToken(): string {
        let token = localStorage.getItem(this.TOKEN_KEY);
        if (token !== null) {
            return token;
        } else {
            return ""
        }
    }
}
