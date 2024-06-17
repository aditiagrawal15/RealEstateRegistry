import { Injectable } from '@angular/core';
import { Observable, of, delay, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginplugin: any = 'http://localhost:9091/registry/user/login';
  private logoutPlugin: any = 'http://localhost:9091/registry/user/logout';
  private setPasswordPlugin: any = 'http://localhost:9091/registry/user/setPassword';
  queryParams: any;
  constructor(private _http: HttpClient) { }

  isLoggedIn() {
    return localStorage.getItem('isUserLoggedIn');
  }

  logIn(aadhar: any, password: any, loggedInRole: any) {
    this.queryParams = { aadharNumber: aadhar, password: password, loggedInRole: loggedInRole }
    return this._http.post<any>(this.loginplugin, this.queryParams);
  }

  setPassword(aadhar: any, password: any) {
    this.queryParams = { aadharNumber: aadhar, password: password }
    return this._http.post<any>(this.setPasswordPlugin, this.queryParams);
  }

  getLogOut(userId: any) {
    return this._http.get<any>(this.logoutPlugin + '/' + userId);
  }

}
