/// <reference types="@types/gapi" />
/// <reference types="@types/gapi.auth2" />
import {Injectable, NgZone} from '@angular/core';
import {BehaviorSubject, Observable, Subscription} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../model/user';
import GoogleAuth = gapi.auth2.GoogleAuth;

@Injectable()
export class AuthenticatorService {
  public auth2: GoogleAuth;
  public user$: BehaviorSubject<User> = new BehaviorSubject<User>(null);
  public isLoggedIn$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public isLoaded$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private zone: NgZone, private http: HttpClient) {
  }

  validateToken(token: string) {
    return this.http.get<null>(`http://localhost:4200/auth/google`, {
      headers: new HttpHeaders({
        ID_TOKEN: token
      })
    });
  }

  signIn(): void {
    this.auth2.signIn().then(user => {
      this.validateToken('tset');
      this.isLoggedIn$.next(true);
      const token = user.getAuthResponse(true).id_token;
      this.validateToken(token).subscribe(x => console.log('token'),
        (error => console.log(error)));
    });

  }

  signOut(): void {
    this.auth2.signOut().then(() => {
        this.zone.run(() => {
          this.isLoggedIn$.next(false);
          this.user$.next(null);
        });
      },
      (err) => {
        console.error(err);
      });
  }

  loadAuth2(): void {
    gapi.load('auth2', () => {
      gapi.auth2.init({
        client_id: '474387750961-ochjmdj1lils7tt350h8oo9ul25hkla6.apps.googleusercontent.com',
        redirect_uri: 'http://localhost:4200/auth/client',
        ux_mode: 'popup'
      }).then((auth) => {
        this.zone.run(() => {
          this.auth2 = auth;
          this.isLoaded$.next(true);
        });
      }, (err) => {
        console.error(err);
      });
    });
  }

}
