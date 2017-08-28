import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OrderItemsComponent } from './order-items.component';
import { OrderItemsDetailComponent } from './order-items-detail.component';
import { OrderItemsPopupComponent } from './order-items-dialog.component';
import { OrderItemsDeletePopupComponent } from './order-items-delete-dialog.component';

export const orderItemsRoute: Routes = [
    {
        path: 'order-items',
        component: OrderItemsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.orderItems.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'order-items/:id',
        component: OrderItemsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.orderItems.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orderItemsPopupRoute: Routes = [
    {
        path: 'order-items-new',
        component: OrderItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.orderItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'order-items/:id/edit',
        component: OrderItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.orderItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'order-items/:id/delete',
        component: OrderItemsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.orderItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
