import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { CartItems } from './cart-items.model';
import { CartItemsService } from './cart-items.service';

@Component({
    selector: 'jhi-cart-items-detail',
    templateUrl: './cart-items-detail.component.html'
})
export class CartItemsDetailComponent implements OnInit, OnDestroy {

    cartItems: CartItems;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cartItemsService: CartItemsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCartItems();
    }

    load(id) {
        this.cartItemsService.find(id).subscribe((cartItems) => {
            this.cartItems = cartItems;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCartItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cartItemsListModification',
            (response) => this.load(this.cartItems.id)
        );
    }
}
