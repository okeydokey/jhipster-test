import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { OrderItems } from './order-items.model';
import { OrderItemsService } from './order-items.service';

@Component({
    selector: 'jhi-order-items-detail',
    templateUrl: './order-items-detail.component.html'
})
export class OrderItemsDetailComponent implements OnInit, OnDestroy {

    orderItems: OrderItems;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private orderItemsService: OrderItemsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOrderItems();
    }

    load(id) {
        this.orderItemsService.find(id).subscribe((orderItems) => {
            this.orderItems = orderItems;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOrderItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'orderItemsListModification',
            (response) => this.load(this.orderItems.id)
        );
    }
}
