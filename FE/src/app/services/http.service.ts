import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class HttpService {
    constructor(private _httpClient:HttpClient) {

    }

    public get = <T = any>(url:string, data:any={}):Observable<T>=>{
        const dataToSend = {
            params:this.prepareQueryStringParams(data)
        };
        return this._httpClient.get<T>(url,dataToSend)
            .pipe(catchError(error=>this.manageErrors(error)));
    }

    public post = <T=any>(url:string, data:any):Observable<T>=>{
        return this._httpClient.post<T>(url,data)
            .pipe(catchError(error=>this.manageErrors(error)));
    }

    public put = <T = any>(url:string, data:any):Observable<T>=>{
        return this._httpClient.put<T>(url,data)
            .pipe(catchError(error=>this.manageErrors(error)));
    }

    public delete = <T = any>(url:string):Observable<T>=>{
        return this._httpClient.delete<T>(url)
            .pipe(catchError(error=>this.manageErrors(error)));
    }

    private manageErrors = (err:any):Observable<any>=>{
        alert(err.error);
        return throwError(()=> err);
        // switch(err.status){
        //     case 400:
        //         break;
        //     case 401:
        //         break;
        //     case 403:
        //         break;
        //     default:
        //         break;
        // }

    }

    private prepareQueryStringParams = (data:any={}):any=>{ 
        let parametersToSend:any={};
        if(data!=null){
            for(let prop in data){
                if(data[prop]!=null && data[prop] != ""){
                    parametersToSend[prop]=data[prop]
                }
            }
        }
        return parametersToSend;
    }
}
