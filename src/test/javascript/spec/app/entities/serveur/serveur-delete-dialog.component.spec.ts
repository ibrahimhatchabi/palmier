import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PalmierDevTestModule } from '../../../test.module';
import { ServeurDeleteDialogComponent } from 'app/entities/serveur/serveur-delete-dialog.component';
import { ServeurService } from 'app/entities/serveur/serveur.service';

describe('Component Tests', () => {
  describe('Serveur Management Delete Component', () => {
    let comp: ServeurDeleteDialogComponent;
    let fixture: ComponentFixture<ServeurDeleteDialogComponent>;
    let service: ServeurService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [ServeurDeleteDialogComponent]
      })
        .overrideTemplate(ServeurDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServeurDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServeurService);
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
