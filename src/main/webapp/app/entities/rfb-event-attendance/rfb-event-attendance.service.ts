import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { RfbEventAttendance } from './rfb-event-attendance.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RfbEventAttendanceService {

    private resourceUrl = SERVER_API_URL + 'api/rfb-event-attendances';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(rfbEventAttendance: RfbEventAttendance): Observable<RfbEventAttendance> {
        return this.http.post(this.resourceUrl, rfbEventAttendance).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(rfbEventAttendance: RfbEventAttendance): Observable<RfbEventAttendance> {
        const copy = this.convert(rfbEventAttendance);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<RfbEventAttendance> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to RfbEventAttendance.
     */
    private convertItemFromServer(json: any): RfbEventAttendance {
        const entity: RfbEventAttendance = Object.assign(new RfbEventAttendance(), json);
        entity.attendanceDate = this.dateUtils
            .convertLocalDateFromServer(json.attendanceDate);
        return entity;
    }

    /**
     * Convert a RfbEventAttendance to a JSON which can be sent to the server.
     */
    private convert(rfbEventAttendance: RfbEventAttendance): RfbEventAttendance {
        const copy: RfbEventAttendance = Object.assign({}, rfbEventAttendance);
        copy.attendanceDate = this.dateUtils
            .convertLocalDateToServer(rfbEventAttendance.attendanceDate);
        return copy;
    }
}
