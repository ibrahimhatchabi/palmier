import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { CiviliteUpdateComponent } from 'app/entities/civilite/civilite-update.component';
import { CiviliteService } from 'app/entities/civilite/civilite.service';
import { Civilite } from 'app/shared/model/civilite.model';

describe('Component Tests', () => {
  describe('Civilite Management Update Component', () => {
    let comp: CiviliteUpdateComponent;
    let fixture: ComponentFixture<CiviliteUpdateComponent>;
    let service: CiviliteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [CiviliteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CiviliteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CiviliteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CiviliteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Civilite(123);
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
        const entity = new Civilite();
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
