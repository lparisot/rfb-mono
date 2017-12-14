import { Component, OnInit } from '@angular/core';
import {DateFormatter} from '@angular/common/src/pipes/intl';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { RfbLocationService } from '../entities/rfb-location/rfb-location.service';
import { ResponseWrapper } from '../shared/model/response-wrapper.model';
import { RfbLocation } from '../entities/rfb-location/rfb-location.model';
import { RfbEventService } from '../entities/rfb-event/rfb-event.service';
import { AccountService } from '../shared/auth/account.service';
import { RfbEvent } from '../entities/rfb-event/rfb-event.model';
import { RfbUser } from '../entities/rfb-user/rfb-user.model';
import {RfbEventAttendance} from '../entities/rfb-event-attendance/rfb-event-attendance.model';
import {RfbEventAttendanceService} from '../entities/rfb-event-attendance/rfb-event-attendance.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    isSaving: boolean;
    locations: RfbLocation[];
    todaysEvent: RfbEvent;
    currentUser: RfbUser;
    model: any;
    rfbEventAttendance: RfbEventAttendance;
    errors: any = {invalidEventCode: ''};
    checkedIn = false;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private locationService: RfbLocationService,
        private eventService: RfbEventService,
        private accountService: AccountService,
        private rfbEventAttendanceService: RfbEventAttendanceService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.loadLocations();
        this.model = {location: 0, eventCode: ''};
        this.rfbEventAttendance = new RfbEventAttendance(null, new Date(), new RfbEvent(), new RfbUser());

        // get current user and if they have a role of organizer show todays event for their home location
        this.accountService.getrfb().subscribe( (user: RfbUser) => {
            this.currentUser = user;
            this.rfbEventAttendance.rfbUserDTO = user;
            // we can set todays event for anyone who has a homeLocation. If they don't we should setTodays event
            // when they change the location drop down || or just grab the event and then compare their event code to the events
            if (this.currentUser.userDTO.authorities.indexOf('ROLE_ORGANIZER') !== -1) {
                this.setTodaysEvent(this.currentUser.rfbLocationDTO.id);
            }
            if (this.currentUser.userDTO.authorities.indexOf('ROLE_RUNNER') !== -1) {
                // set home location
                this.model.location = this.currentUser.rfbLocationDTO.id;
            }
        });
    }

    setTodaysEvent(locationID: number) {
        // reach out to our event service and find an event with todays date & this location id: homeLocationId;
        this.eventService.findByLocation(locationID).subscribe( (rfbEvent: RfbEvent) => {
            this.todaysEvent = rfbEvent;
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadLocations() {
        this.locationService.query({
            page: 0,
            size: 100,
            sort: ['locationName', 'ASC']}).subscribe(
            (res: ResponseWrapper) => {
                this.locations = res.json;
            },
            (res: ResponseWrapper) => { console.log(res) }
        );
    }

    checkIn() {
        this.eventService.findByLocation(this.model.location).subscribe( (rfbEvent: RfbEvent) => {
            const thisEvent = rfbEvent;
            this.rfbEventAttendance.rfbEventDTO = rfbEvent;
            if (thisEvent.eventCode === this.model.eventCode) {
                // you are checked in
                this.rfbEventAttendanceService.create(this.rfbEventAttendance).subscribe( (userCheckedIn: RfbEventAttendance) => {
                    this.checkedIn = true;

                    console.log('USER is checked in!');
                });
            } else {
                this.errors.invalidEventCode = 'Wrong event code!';
                console.log('There is either no event today or you have the wrong event code!');
            }
        });
    }

    clear() {}

    setHomeLocation() {}
}
