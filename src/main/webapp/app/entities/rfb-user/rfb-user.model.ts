import { BaseEntity } from './../../shared';

export class RfbUser implements BaseEntity {
    constructor(
        public id?: number,
        public userName?: string,
        public homeLocationId?: number,
        public userId?: number,
        public rfbEventAttendances?: BaseEntity[],
    ) {
    }
}
