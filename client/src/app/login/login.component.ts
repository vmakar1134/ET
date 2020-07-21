import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  baseURL = 'https://accounts.google.com/o/oauth2/v2/auth';
  clientId = '474387750961-ochjmdj1lils7tt350h8oo9ul25hkla6.apps.googleusercontent.com';
  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  onClick() {
    const params = new HttpParams()
      .set('scope', 'profile')
      .set('access_type', 'offline')
      .set('include_granted_scopes', 'true')
      .set('response_type', 'code')
      .set('redirect_uri', 'http://localhost:4200/')
      .set('client_id', this.clientId);
    const href = this.baseURL + '?' + params;
    console.log(href);
    window.location.href = href;
  }

}
