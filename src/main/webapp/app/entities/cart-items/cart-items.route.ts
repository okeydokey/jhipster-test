import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CartItemsComponent } from './cart-items.component';
import { CartItemsDetailComponent } from './cart-items-detail.component';
import { CartItemsPopupComponent } from './cart-items-dialog.component';
import { CartItemsDeletePopupComponent } from './cart-items-delete-dialog.component';

export const cartItemsRoute: Routes = [
    {
        path: 'cart-items',
        component: CartItemsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.cartItems.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cart-items/:id',
        component: CartItemsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.cartItems.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cartItemsPopupRoute: Routes = [
    {
        path: 'cart-items-new',
        component: CartItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.cartItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cart-items/:id/edit',
        component: CartItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.cartItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cart-items/:id/delete',
        component: CartItemsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterTestApp.cartItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
