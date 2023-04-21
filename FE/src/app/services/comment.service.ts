import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { urlBaseBE } from 'src/environments/environment';
import { PatchDto } from '../models/models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  URL = urlBaseBE + "/api/comment"
  comments: Comment[] = []

  constructor(private http: HttpClient) { }

  //PATCH
  public modifyComment(commentId: number, patchDto: PatchDto): Observable<any>{
    return this.http.patch<any>(`${this.URL}/update/${commentId}`, patchDto)
  };
}
