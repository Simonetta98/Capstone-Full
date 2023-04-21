import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PatchDto, User } from '../models/models';
import { urlBaseBE } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  URL = urlBaseBE + "/api/user"
  users: User[] = []

  constructor(private http: HttpClient) { }

//GET
  public getAllUsers(): Observable<any>{
    return this.http.get<any>(`${this.URL}/all/0`)
  };

  public getUserById(userId: number): Observable<User>{
    return this.http.get<User>(`${this.URL}/${userId}`)
  };

  public getUserByUsername(username: string): Observable<User>{
    return this.http.get<User>(`${this.URL}/username/${username}`)
  };

  public getUserByProfile(profileId: number): Observable<User>{
    return this.http.get<User>(`${this.URL}/by/profile/${profileId}`)
  };

//PATCH
public modifyUser(userId: number, patchDto: PatchDto): Observable<any>{
    return this.http.patch<any>(`${this.URL}/update/${userId}`, patchDto)
  };

//DELETE
  public deleteUser(userId: number): Observable<void>{
    return this.http.delete<void>(`${this.URL}/delete/${userId}`)
  };
}
