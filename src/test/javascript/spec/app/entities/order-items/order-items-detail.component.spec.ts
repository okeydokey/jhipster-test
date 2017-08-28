/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JhipsterTestTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OrderItemsDetailComponent } from '../../../../../../main/webapp/app/entities/order-items/order-items-detail.component';
import { OrderItemsService } from '../../../../../../main/webapp/app/entities/order-items/order-items.service';
import { OrderItems } from '../../../../../../main/webapp/app/entities/order-items/order-items.model';

describe('Component Tests', () => {

    describe('OrderItems Management Detail Component', () => {
        let comp: OrderItemsDetailComponent;
        let fixture: ComponentFixture<OrderItemsDetailComponent>;
        let service: OrderItemsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestTestModule],
                declarations: [OrderItemsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OrderItemsService,
                    JhiEventManager
                ]
            }).overrideTemplate(OrderItemsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrderItemsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderItemsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new OrderItems(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.orderItems).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
