import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PalmierDevTestModule } from '../../../test.module';
import { ServeurDetailComponent } from 'app/entities/serveur/serveur-detail.component';
import { Serveur } from 'app/shared/model/serveur.model';

describe('Component Tests', () => {
  describe('Serveur Management Detail Component', () => {
    let comp: ServeurDetailComponent;
    let fixture: ComponentFixture<ServeurDetailComponent>;
    const route = ({ data: of({ serveur: new Serveur(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PalmierDevTestModule],
        declarations: [ServeurDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ServeurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServeurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.serveur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
