import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { TypePlatUpdateComponent } from 'app/entities/type-plat/type-plat-update.component';
import { TypePlatService } from 'app/entities/type-plat/type-plat.service';
import { TypePlat } from 'app/shared/model/type-plat.model';

describe('Component Tests', () => {
  describe('TypePlat Management Update Component', () => {
    let comp: TypePlatUpdateComponent;
    let fixture: ComponentFixture<TypePlatUpdateComponent>;
    let service: TypePlatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [TypePlatUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TypePlatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypePlatUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypePlatService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TypePlat(123);
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
        const entity = new TypePlat();
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
