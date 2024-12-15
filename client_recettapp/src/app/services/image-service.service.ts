import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MultiplyArray } from 'socket.io/dist/typed-events';

@Injectable({
  providedIn: 'root'
})
export class ImageServiceService {


  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/image";

  /**
   * Uploads an image to the server.
   * @param image - The image file to upload.
   * @returns An Observable of the server response.
   */
  addImage(image: File): Observable<string> {
    const formData = new FormData();
    formData.append('image', image);

    return this.http.post<string>(this.Url, formData);
  }
}
