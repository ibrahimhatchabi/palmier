import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { BoissonDetailComponent } from 'app/entities/boisson/boisson-detail.component';
import { Boisson } from 'app/shared/model/boisson.model';

describe('Component Tests', () => {
  describe('Boisson Management Detail Component', () => {
    let comp: BoissonDetailComponent;
    let fixture: ComponentFixture<BoissonDetailComponent>;
    const route = ({ data: of({ boisson: new Boisson(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [BoissonDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BoissonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoissonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boisson).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
