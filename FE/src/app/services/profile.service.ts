import { Injectable } from '@angular/core';
import { PatchDto, Post, UserProfile } from '../models/models';
import { urlBaseBE } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, share } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  URL = urlBaseBE + "/api/userprofile"
  profiles: UserProfile[] = []

  constructor(private http: HttpClient) { }

//POST
  public createProfile(userId: number, profile: UserProfile): Observable<any>{
    return this.http.post<any>(`${this.URL}/for/${userId}`, profile)
  };

  public createLike(profileId: number, postId: number): Observable<any>{
    return this.http.post<any>(`${this.URL}/likes/${profileId}/${postId}`, null)
  };

//GET
  public getAllProfiles(): Observable<any>{
    return this.http.get<any>(`${this.URL}/all/0`)
  };

  public getProfileById(userId: number): Observable<UserProfile>{
    return this.http.get<UserProfile>(`${this.URL}/${userId}`)
  };

  public getProfileByUsername(username: string): Observable<UserProfile>{
    return this.http.get<UserProfile>(`${this.URL}/username/${username}`)
  };

  public getProfilePosts(profileId: number): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.URL}/posts/${profileId}`)
  };

  public getProfileByPost(postId: number): Observable<UserProfile>{
    return this.http.get<UserProfile>(`${this.URL}/by/post/${postId}`)
  };
//vedi chi ha messo like nei post
  public getProfilePostLikes(postId: number): Observable<UserProfile[]>{
    return this.http.get<UserProfile[]>(`${this.URL}/likes/${postId}`)
  };
//vedi in quali post hai messo like
  public getProfileLikedPosts(profileId: number): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.URL}/likes/posts/${profileId}`)
  };

//PATCH
  public modifyProfile(profileId: number, patchDto: PatchDto): Observable<any>{
    return this.http.patch<any>(`${this.URL}/update/${profileId}`, patchDto)
  };

//DELETE
  public deleteProfile(profileId: number): Observable<void>{
    return this.http.delete<void>(`${this.URL}/delete/${profileId}`)
  };

  public deleteLike(profileId: number, postId: number): Observable<void>{
    return this.http.delete<void>(`${this.URL}/remove/like/${profileId}/${postId}`)
  };
}
