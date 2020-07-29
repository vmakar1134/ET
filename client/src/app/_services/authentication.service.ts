import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {UserAuth, UserProfile} from '../models';
import {environment} from '../../environments/environment';
import {catchError} from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<UserProfile>;
  public currentUser: Observable<UserProfile>;
  private baseUrl = environment.baseUrl;


  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<UserProfile>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  google(): Observable<{ status: any; }> {
    const url = `${this.baseUrl}/google`;
    return this.http.get(url, {observe: 'response'})
      .pipe(catchError(this.handleError));
  }

  register(userAuth: UserAuth): Observable<{ status: any; }> {
    const url = `${this.baseUrl}/auth/register`;
    return this.http.post(url, userAuth, {observe: 'response'})
      .pipe(catchError(this.handleError));
  }

  login(userAuth: UserAuth) {
    return this.http.post<any>(`${environment.baseUrl}/auth/login`, {userAuth})
      .subscribe(token => localStorage.setItem('token', JSON.stringify(token)));
  }

  logout() {
    this.http.get(`${environment.baseUrl}/auth/logout`);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }
}
