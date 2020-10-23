import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { PlatUpdateComponent } from 'app/entities/plat/plat-update.component';
import { PlatService } from 'app/entities/plat/plat.service';
import { Plat } from 'app/shared/model/plat.model';

describe('Component Tests', () => {
  describe('Plat Management Update Component', () => {
    let comp: PlatUpdateComponent;
    let fixture: ComponentFixture<PlatUpdateComponent>;
    let service: PlatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [PlatUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlatUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlatService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Plat(123);
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
        const entity = new Plat();
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
