import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { TypePlatDetailComponent } from 'app/entities/type-plat/type-plat-detail.component';
import { TypePlat } from 'app/shared/model/type-plat.model';

describe('Component Tests', () => {
  describe('TypePlat Management Detail Component', () => {
    let comp: TypePlatDetailComponent;
    let fixture: ComponentFixture<TypePlatDetailComponent>;
    const route = ({ data: of({ typePlat: new TypePlat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [TypePlatDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TypePlatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypePlatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typePlat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
