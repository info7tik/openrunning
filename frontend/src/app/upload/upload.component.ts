import { DOCUMENT } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { ApiRequestService } from '../api-request.service';
import { TokenManagerService } from '../token-manager.service';

@Component({
    selector: 'app-upload',
    templateUrl: './upload.component.html',
    styleUrls: ['./upload.component.css']
})
export class UploadComponent {
    files: File[] = [];

    constructor(private apiRequest: ApiRequestService, private tokenManager: TokenManagerService,
        @Inject(DOCUMENT) private document: Document) { }

    onFileSelected(event: Event): void {
        this.files = [];
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
                const upload = this.apiRequest.postWithDefaultHeaders("gpx/upload", formData);
                upload.subscribe({
                    next: data => {
                        let allFilesDiv = this.document.getElementsByClassName("file-selected");
                        let fileToUploadDiv = Array.prototype.filter.call(
                            allFilesDiv, (div) => div.textContent.trim() === data.filename);
                        fileToUploadDiv[0].style.backgroundColor = "#d5fce8";
                    },
                    error: error => {
                        let allFilesDiv = this.document.getElementsByClassName("file-selected");
                        let fileToUploadDiv = Array.prototype.filter.call(
                            allFilesDiv, (div) => div.textContent.trim() === error.filename);
                        fileToUploadDiv[0].style.backgroundColor = "#fcd9d5";
                    }
                });
            }
        });
    }

    checkUploaded(): void {

    }
}
