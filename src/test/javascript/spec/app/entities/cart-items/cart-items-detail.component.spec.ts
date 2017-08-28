/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JhipsterTestTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CartItemsDetailComponent } from '../../../../../../main/webapp/app/entities/cart-items/cart-items-detail.component';
import { CartItemsService } from '../../../../../../main/webapp/app/entities/cart-items/cart-items.service';
import { CartItems } from '../../../../../../main/webapp/app/entities/cart-items/cart-items.model';

describe('Component Tests', () => {

    describe('CartItems Management Detail Component', () => {
        let comp: CartItemsDetailComponent;
        let fixture: ComponentFixture<CartItemsDetailComponent>;
        let service: CartItemsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestTestModule],
                declarations: [CartItemsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CartItemsService,
                    JhiEventManager
                ]
            }).overrideTemplate(CartItemsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CartItemsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CartItemsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CartItems(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cartItems).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
