import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbSharedModule } from '../../shared';
import { RfbAdminModule } from '../../admin/admin.module';
import {
    RfbUserService,
    RfbUserPopupService,
    RfbUserComponent,
    RfbUserDetailComponent,
    RfbUserDialogComponent,
    RfbUserPopupComponent,
    RfbUserDeletePopupComponent,
    RfbUserDeleteDialogComponent,
    rfbUserRoute,
    rfbUserPopupRoute,
} from './';

const ENTITY_STATES = [
    ...rfbUserRoute,
    ...rfbUserPopupRoute,
];

@NgModule({
    imports: [
        RfbSharedModule,
        RfbAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RfbUserComponent,
        RfbUserDetailComponent,
        RfbUserDialogComponent,
        RfbUserDeleteDialogComponent,
        RfbUserPopupComponent,
        RfbUserDeletePopupComponent,
    ],
    entryComponents: [
        RfbUserComponent,
        RfbUserDialogComponent,
        RfbUserPopupComponent,
        RfbUserDeleteDialogComponent,
        RfbUserDeletePopupComponent,
    ],
    providers: [
        RfbUserService,
        RfbUserPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbRfbUserModule {}
