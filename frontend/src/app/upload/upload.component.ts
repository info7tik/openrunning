import { Component } from '@angular/core';
import { ApiRequestService } from '../api-request.service';
import { TokenManagerService } from '../token-manager.service';

@Component({
    selector: 'app-upload',
    templateUrl: './upload.component.html',
    styleUrls: ['./upload.component.css']
})
export class UploadComponent {
    files: File[] = [];

    constructor(private apiRequest: ApiRequestService, private tokenManager: TokenManagerService) { }

    onFileSelected(event: Event): void {
        const target = event.target as HTMLInputElement;
        if (target.files !== null) {
            for (let i = 0; i < target.files.length; i++) {
                const fileItem = target.files.item(i);
                if (fileItem !== null) {
                    this.files.push(fileItem);
                }
            }
        }
    }

    uploadSelectedFiles(): void {
        this.files.forEach(file => {
            if (file) {
                const formData = new FormData();
                formData.append("file", file);
                formData.append("email", this.tokenManager.getEmail());
                const upload$ = this.apiRequest.postWithDefaultHeaders("gpx/upload", formData);
                upload$.subscribe({
                    next: data => {
                        alert(data);
                    }
                });
            }
        });
    }

    checkUploaded(): void {

    }
}
