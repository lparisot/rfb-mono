import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal, AccountService, JhiLanguageHelper, ResponseWrapper } from '../../shared';
import { RfbLocation } from '../../entities/rfb-location/rfb-location.model';
import { RfbLocationService } from '../../entities/rfb-location/rfb-location.service';
import { RfbUser } from '../../entities/rfb-user/rfb-user.model';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    locations: RfbLocation[];

    constructor(
        private accountService: AccountService,
        private principal: Principal,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private locationService: RfbLocationService
    ) {
    }

    ngOnInit() {
        this.loadLocations();
        this.principal.identity().then((account) => {
            this.settingsAccount = this.copyAccount(account);
            this.loadLocation();
        });
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
    }

    loadLocation() {
        this.accountService.getrfb().subscribe( (user: RfbUser) => {
            if (user != null) {
                this.settingsAccount.homeLocation = user.rfbLocationDTO.id;
            }
        });
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

    save() {
        this.accountService.save(this.settingsAccount).subscribe(() => {
            this.error = null;
            this.success = 'OK';
            this.principal.identity(true).then((account) => {
                this.settingsAccount = this.copyAccount(account);
            });
            this.languageService.getCurrent().then((current) => {
                if (this.settingsAccount.langKey !== current) {
                    this.languageService.changeLanguage(this.settingsAccount.langKey);
                }
            });
            if (this.settingsAccount != null) {
                this.accountService.changeLocation(this.settingsAccount.homeLocation).subscribe(() => this.loadLocation());
            }
        }, () => {
            this.success = null;
            this.error = 'ERROR';
        });

    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl,
            homeLocation: account.homeLocation
        };
    }

    trackRfbLocationById(index: number, item: RfbLocation) {
        return item.id;
    }
}
