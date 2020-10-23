import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PalmierDevTestModule } from '../../../test.module';
import { BoissonDeleteDialogComponent } from 'app/entities/boisson/boisson-delete-dialog.component';
import { BoissonService } from 'app/entities/boisson/boisson.service';

describe('Component Tests', () => {
  describe('Boisson Management Delete Component', () => {
    let comp: BoissonDeleteDialogComponent;
    let fixture: ComponentFixture<BoissonDeleteDialogComponent>;
    let service: BoissonService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [BoissonDeleteDialogComponent]
      })
        .overrideTemplate(BoissonDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoissonDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BoissonService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
