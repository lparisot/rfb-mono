import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { RfbEvent } from './rfb-event.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RfbEventService {

    private resourceUrl = SERVER_API_URL + 'api/rfb-events';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(rfbEvent: RfbEvent): Observable<RfbEvent> {
        const copy = this.convert(rfbEvent);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(rfbEvent: RfbEvent): Observable<RfbEvent> {
        const copy = this.convert(rfbEvent);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<RfbEvent> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    findByLocation(locationId: number): Observable<RfbEvent> {
        return this.http.get(`${this.resourceUrl}/location/${locationId}`).map((res: Response) => {
            return res.json();
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
     * Convert a returned JSON object to RfbEvent.
     */
    private convertItemFromServer(json: any): RfbEvent {
        const entity: RfbEvent = Object.assign(new RfbEvent(), json);
        entity.eventDate = this.dateUtils
            .convertLocalDateFromServer(json.eventDate);
        return entity;
    }

    /**
     * Convert a RfbEvent to a JSON which can be sent to the server.
     */
    private convert(rfbEvent: RfbEvent): RfbEvent {
        const copy: RfbEvent = Object.assign({}, rfbEvent);
        copy.eventDate = this.dateUtils
            .convertLocalDateToServer(rfbEvent.eventDate);
        return copy;
    }
}
