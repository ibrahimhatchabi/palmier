import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { BoissonUpdateComponent } from 'app/entities/boisson/boisson-update.component';
import { BoissonService } from 'app/entities/boisson/boisson.service';
import { Boisson } from 'app/shared/model/boisson.model';

describe('Component Tests', () => {
  describe('Boisson Management Update Component', () => {
    let comp: BoissonUpdateComponent;
    let fixture: ComponentFixture<BoissonUpdateComponent>;
    let service: BoissonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [BoissonUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BoissonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoissonUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BoissonService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Boisson(123);
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
        const entity = new Boisson();
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
