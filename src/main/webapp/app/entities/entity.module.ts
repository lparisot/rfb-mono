import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RfbRfbLocationModule } from './rfb-location/rfb-location.module';
import { RfbRfbEventModule } from './rfb-event/rfb-event.module';
import { RfbRfbEventAttendanceModule } from './rfb-event-attendance/rfb-event-attendance.module';
import { RfbRfbUserModule } from './rfb-user/rfb-user.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RfbRfbLocationModule,
        RfbRfbEventModule,
        RfbRfbEventAttendanceModule,
        RfbRfbUserModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbEntityModule {}
