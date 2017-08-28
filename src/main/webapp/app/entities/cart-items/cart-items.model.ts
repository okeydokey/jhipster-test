import { BaseEntity } from './../../shared';

export class CartItems implements BaseEntity {
    constructor(
        public id?: number,
        public customerId?: number,
        public productId?: number,
    ) {
    }
}
