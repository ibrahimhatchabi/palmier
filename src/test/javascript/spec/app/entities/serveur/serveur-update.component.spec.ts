import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { ServeurUpdateComponent } from 'app/entities/serveur/serveur-update.component';
import { ServeurService } from 'app/entities/serveur/serveur.service';
import { Serveur } from 'app/shared/model/serveur.model';

describe('Component Tests', () => {
  describe('Serveur Management Update Component', () => {
    let comp: ServeurUpdateComponent;
    let fixture: ComponentFixture<ServeurUpdateComponent>;
    let service: ServeurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [ServeurUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ServeurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServeurUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServeurService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Serveur(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Serveur();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
