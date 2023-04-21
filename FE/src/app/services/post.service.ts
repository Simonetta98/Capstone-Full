import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { urlBaseBE } from 'src/environments/environment';
import { Comment, PatchDto, Post } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  URL = urlBaseBE + "/api/post"
  posts: Post[] = []

  constructor(private http: HttpClient) { }

//POST
  public createPost(post: Post, profileId: number): Observable<any>{
    return this.http.post<any>(`${this.URL}/create/${profileId}`, post)
  };

  public createComment(postId: number, profileId: number, c: Comment): Observable<any>{
    return this.http.post<any>(`${this.URL}/comment/${postId}/${profileId}`, c)
  };

//GET
  public getAllPosts(): Observable<any>{
    return this.http.get<any>(`${this.URL}/all/0`)
  };

  public getPostById(userId: number): Observable<any>{
    return this.http.get<any>(`${this.URL}/${userId}`)
  };

  public getAllComments(postId: number): Observable<any>{
    return this.http.get<any>(`${this.URL}/all/comments/${postId}`)
  };

//PATCH
  public modifyPost(postId: number, patchDto: PatchDto): Observable<any>{
    return this.http.patch<any>(`${this.URL}/update/${postId}`, patchDto)
  };

//DELETE
  public deletePost(postId: number): Observable<void>{
    return this.http.delete<void>(`${this.URL}/delete/${postId}`)
  };

  public deleteComment(commentId: number, postId: number): Observable<void>{
    return this.http.delete<void>(`${this.URL}/comment/delete/${commentId}/${postId}`)
  };

}
