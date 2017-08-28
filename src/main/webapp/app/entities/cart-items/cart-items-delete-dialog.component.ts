import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CartItems } from './cart-items.model';
import { CartItemsPopupService } from './cart-items-popup.service';
import { CartItemsService } from './cart-items.service';

@Component({
    selector: 'jhi-cart-items-delete-dialog',
    templateUrl: './cart-items-delete-dialog.component.html'
})
export class CartItemsDeleteDialogComponent {

    cartItems: CartItems;

    constructor(
        private cartItemsService: CartItemsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cartItemsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cartItemsListModification',
                content: 'Deleted an cartItems'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cart-items-delete-popup',
    template: ''
})
export class CartItemsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cartItemsPopupService: CartItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cartItemsPopupService
                .open(CartItemsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
