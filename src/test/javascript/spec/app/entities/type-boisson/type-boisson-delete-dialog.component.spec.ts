import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PalmierDevTestModule } from '../../../test.module';
import { TypeBoissonDeleteDialogComponent } from 'app/entities/type-boisson/type-boisson-delete-dialog.component';
import { TypeBoissonService } from 'app/entities/type-boisson/type-boisson.service';

describe('Component Tests', () => {
  describe('TypeBoisson Management Delete Component', () => {
    let comp: TypeBoissonDeleteDialogComponent;
    let fixture: ComponentFixture<TypeBoissonDeleteDialogComponent>;
    let service: TypeBoissonService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [TypeBoissonDeleteDialogComponent]
      })
        .overrideTemplate(TypeBoissonDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeBoissonDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypeBoissonService);
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
