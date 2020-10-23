import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { TypeBoissonUpdateComponent } from 'app/entities/type-boisson/type-boisson-update.component';
import { TypeBoissonService } from 'app/entities/type-boisson/type-boisson.service';
import { TypeBoisson } from 'app/shared/model/type-boisson.model';

describe('Component Tests', () => {
  describe('TypeBoisson Management Update Component', () => {
    let comp: TypeBoissonUpdateComponent;
    let fixture: ComponentFixture<TypeBoissonUpdateComponent>;
    let service: TypeBoissonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [TypeBoissonUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TypeBoissonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeBoissonUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypeBoissonService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TypeBoisson(123);
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
        const entity = new TypeBoisson();
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
