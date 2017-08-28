import { BaseEntity } from './../../shared';

const enum CustomerLevel {
    'NEW',
    'SILVER',
    'GOLD',
    'VIP',
    'VVIP',
    'FIRST'
}

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public customerLevel?: CustomerLevel,
        public phone?: string,
        public userId?: number,
        public orders?: BaseEntity[],
        public cartItems?: BaseEntity[],
    ) {
    }
}
