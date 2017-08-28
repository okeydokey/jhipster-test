import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterTestSharedModule } from '../../shared';
import {
    OrdersService,
    OrdersPopupService,
    OrdersComponent,
    OrdersDetailComponent,
    OrdersDialogComponent,
    OrdersPopupComponent,
    OrdersDeletePopupComponent,
    OrdersDeleteDialogComponent,
    ordersRoute,
    ordersPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ordersRoute,
    ...ordersPopupRoute,
];

@NgModule({
    imports: [
        JhipsterTestSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OrdersComponent,
        OrdersDetailComponent,
        OrdersDialogComponent,
        OrdersDeleteDialogComponent,
        OrdersPopupComponent,
        OrdersDeletePopupComponent,
    ],
    entryComponents: [
        OrdersComponent,
        OrdersDialogComponent,
        OrdersPopupComponent,
        OrdersDeleteDialogComponent,
        OrdersDeletePopupComponent,
    ],
    providers: [
        OrdersService,
        OrdersPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTestOrdersModule {}
