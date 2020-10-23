import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PalmierDevTestModule } from '../../../test.module';
import { TypePlatDeleteDialogComponent } from 'app/entities/type-plat/type-plat-delete-dialog.component';
import { TypePlatService } from 'app/entities/type-plat/type-plat.service';

describe('Component Tests', () => {
  describe('TypePlat Management Delete Component', () => {
    let comp: TypePlatDeleteDialogComponent;
    let fixture: ComponentFixture<TypePlatDeleteDialogComponent>;
    let service: TypePlatService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [TypePlatDeleteDialogComponent]
      })
        .overrideTemplate(TypePlatDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypePlatDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypePlatService);
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
