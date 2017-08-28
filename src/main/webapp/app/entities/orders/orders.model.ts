import { BaseEntity } from './../../shared';

export class Orders implements BaseEntity {
    constructor(
        public id?: number,
        public orderDate?: any,
        public customerId?: number,
        public orderItems?: BaseEntity[],
    ) {
    }
}
