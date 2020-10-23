import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { CiviliteDetailComponent } from 'app/entities/civilite/civilite-detail.component';
import { Civilite } from 'app/shared/model/civilite.model';

describe('Component Tests', () => {
  describe('Civilite Management Detail Component', () => {
    let comp: CiviliteDetailComponent;
    let fixture: ComponentFixture<CiviliteDetailComponent>;
    const route = ({ data: of({ civilite: new Civilite(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [CiviliteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CiviliteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CiviliteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.civilite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
