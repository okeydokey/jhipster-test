import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterTestSharedModule } from '../../shared';
import {
    CartItemsService,
    CartItemsPopupService,
    CartItemsComponent,
    CartItemsDetailComponent,
    CartItemsDialogComponent,
    CartItemsPopupComponent,
    CartItemsDeletePopupComponent,
    CartItemsDeleteDialogComponent,
    cartItemsRoute,
    cartItemsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cartItemsRoute,
    ...cartItemsPopupRoute,
];

@NgModule({
    imports: [
        JhipsterTestSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CartItemsComponent,
        CartItemsDetailComponent,
        CartItemsDialogComponent,
        CartItemsDeleteDialogComponent,
        CartItemsPopupComponent,
        CartItemsDeletePopupComponent,
    ],
    entryComponents: [
        CartItemsComponent,
        CartItemsDialogComponent,
        CartItemsPopupComponent,
        CartItemsDeleteDialogComponent,
        CartItemsDeletePopupComponent,
    ],
    providers: [
        CartItemsService,
        CartItemsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTestCartItemsModule {}
