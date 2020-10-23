import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PalmierDevTestModule } from '../../../test.module';
import { CiviliteDeleteDialogComponent } from 'app/entities/civilite/civilite-delete-dialog.component';
import { CiviliteService } from 'app/entities/civilite/civilite.service';

describe('Component Tests', () => {
  describe('Civilite Management Delete Component', () => {
    let comp: CiviliteDeleteDialogComponent;
    let fixture: ComponentFixture<CiviliteDeleteDialogComponent>;
    let service: CiviliteService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [CiviliteDeleteDialogComponent]
      })
        .overrideTemplate(CiviliteDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CiviliteDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CiviliteService);
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
