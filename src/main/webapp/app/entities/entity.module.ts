import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JhipsterTestCustomerModule } from './customer/customer.module';
import { JhipsterTestOrdersModule } from './orders/orders.module';
import { JhipsterTestOrderItemsModule } from './order-items/order-items.module';
import { JhipsterTestProductModule } from './product/product.module';
import { JhipsterTestCartItemsModule } from './cart-items/cart-items.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JhipsterTestCustomerModule,
        JhipsterTestOrdersModule,
        JhipsterTestOrderItemsModule,
        JhipsterTestProductModule,
        JhipsterTestCartItemsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTestEntityModule {}
