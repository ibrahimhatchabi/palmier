import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { PlatDetailComponent } from 'app/entities/plat/plat-detail.component';
import { Plat } from 'app/shared/model/plat.model';

describe('Component Tests', () => {
  describe('Plat Management Detail Component', () => {
    let comp: PlatDetailComponent;
    let fixture: ComponentFixture<PlatDetailComponent>;
    const route = ({ data: of({ plat: new Plat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [PlatDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.plat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
