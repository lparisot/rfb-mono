import { SpyObject } from './spyobject';
import { AccountService } from '../../../../main/webapp/app/shared/auth/account.service';
import Spy = jasmine.Spy;

export class MockAccountService extends SpyObject {

    getSpy: Spy;
    saveSpy: Spy;
    getrfbSpy: Spy;
    changeLocationSpy: Spy;
    fakeResponse: any;

    constructor() {
        super(AccountService);

        this.fakeResponse = null;
        this.getSpy = this.spy('get').andReturn(this);
        this.saveSpy = this.spy('save').andReturn(this);
        this.getrfbSpy = this.spy('getrfb').andReturn(this);
        this.changeLocationSpy = this.spy('changeLocation').andReturn(this);
    }

    subscribe(callback: any) {
        callback(this.fakeResponse);
    }

    setResponse(json: any): void {
        this.fakeResponse = json;
    }
}
