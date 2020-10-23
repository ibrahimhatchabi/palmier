import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { TypeBoissonDetailComponent } from 'app/entities/type-boisson/type-boisson-detail.component';
import { TypeBoisson } from 'app/shared/model/type-boisson.model';

describe('Component Tests', () => {
  describe('TypeBoisson Management Detail Component', () => {
    let comp: TypeBoissonDetailComponent;
    let fixture: ComponentFixture<TypeBoissonDetailComponent>;
    const route = ({ data: of({ typeBoisson: new TypeBoisson(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [TypeBoissonDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TypeBoissonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeBoissonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeBoisson).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
