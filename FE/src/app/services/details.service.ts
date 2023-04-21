import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { User } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class DetailsService {

buttonClickSubject = new BehaviorSubject<null | any>(null);
buttonClick$ = this.buttonClickSubject.asObservable();

  constructor() { }
}
