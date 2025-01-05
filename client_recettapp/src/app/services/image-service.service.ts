import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MultiplyArray } from 'socket.io/dist/typed-events';
import { Recipe } from '../models/recipe';
import { Evaluation } from '../models/evaluation';

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
  addImage(image: File, recipeToAdd: Recipe, token: any): Observable<string> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData = new FormData();
    formData.append('image', image);
    formData.append('recipe', String(recipeToAdd.id));

    return this.http.post<string>(this.Url, formData, {headers});
  }

  getImage(fileName: string, token: any): Observable<Blob> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.Url}/${fileName}`, { responseType: 'blob', headers });
  }

  addImageEvaluation(image : File, evaluation: Evaluation, token: any): Observable<string>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData = new FormData();
    formData.append('image', image);
    formData.append('evaluation', String(evaluation.id));

    return this.http.post<string>(`${this.Url}/evaluation`, formData, {headers});
  }

}
