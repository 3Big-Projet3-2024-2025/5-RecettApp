import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MultiplyArray } from 'socket.io/dist/typed-events';
import { Recipe } from '../models/recipe';

@Injectable({
  providedIn: 'root'
})
export class ImageServiceService {


  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/image";

  /**
   * Uploads an image to the server.
   * @param image - The image file to upload.
    * @param recipe the  Recipe associated with the image
    * @return {@code boolean}
    */
  addImage(image: File, recipeToAdd: Recipe): Observable<string> {
    const formData = new FormData();
    formData.append('image', image);
    formData.append('recipe', String(recipeToAdd.id));
  
    return this.http.post<string>(this.Url, formData);
  }
  
  getImage(fileName: string): Observable<Blob> {
    return this.http.get(`${this.Url}/${fileName}`, { responseType: 'blob' });
  }
}
