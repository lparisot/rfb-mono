import { BaseEntity } from './../../shared';
import {RfbLocation} from '../rfb-location/rfb-location.model';
import {User} from '../../shared/user/user.model';

export class RfbUser implements BaseEntity {
    constructor(
        public id?: number,
        public userName?: string,
        public rfbLocationDTO?: RfbLocation,
        public userDTO?: User,
        public rfbEventAttendances?: BaseEntity[],
    ) {
    }
}
