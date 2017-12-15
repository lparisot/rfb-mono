import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class AccountService  {
    constructor(private http: Http) { }

    get(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/account').map((res: Response) => res.json());
    }

    getrfb(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/accountr').map((res: Response) => res.json());
    }

    save(account: any): Observable<Response> {
        return this.http.post(SERVER_API_URL + 'api/account', account);
    }

    changeLocation(location: number): Observable<Response> {
        const headers = new Headers ({
            'Content-Type': 'application/json'
        });
        return this.http.post(SERVER_API_URL + 'api/account/change-location', location, { headers });
    }
}
