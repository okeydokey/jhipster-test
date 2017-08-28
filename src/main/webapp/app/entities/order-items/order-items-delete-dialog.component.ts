import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { OrderItems } from './order-items.model';
import { OrderItemsPopupService } from './order-items-popup.service';
import { OrderItemsService } from './order-items.service';

@Component({
    selector: 'jhi-order-items-delete-dialog',
    templateUrl: './order-items-delete-dialog.component.html'
})
export class OrderItemsDeleteDialogComponent {

    orderItems: OrderItems;

    constructor(
        private orderItemsService: OrderItemsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orderItemsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'orderItemsListModification',
                content: 'Deleted an orderItems'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-order-items-delete-popup',
    template: ''
})
export class OrderItemsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderItemsPopupService: OrderItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.orderItemsPopupService
                .open(OrderItemsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
