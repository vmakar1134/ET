/// <reference types="@types/gapi" />
import {Injectable, NgZone} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
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

  validateToken(token: string): Observable<User> {
    return this.http.get<User>(`http://yourServer:3000/validationApi/${token}`);
  }

  signIn(): void {
    this.auth2.signIn().then(user => {
      this.isLoggedIn$.next(true);
      this.user$.next(user);
      console.log(user);
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
      // @ts-ignore
      gapi.auth2.init({
        client_id: '474387750961-ochjmdj1lils7tt350h8oo9ul25hkla6.apps.googleusercontent.com',
        fetch_basic_profile: false,
        scope: 'openid'
      }).then((auth) => {
        this.zone.run(() => {
          this.auth2 = auth;
          this.isLoaded$.next(true);
          console.log('auth');
          console.log(auth);
        });
      }, (err) => {
        console.error(err);
      });
    });
  }

}
