import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServeur } from 'app/shared/model/serveur.model';
import { map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ICivilite } from 'app/shared/model/civilite.model';
import { CiviliteService } from 'app/entities/civilite/civilite.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-serveur-detail',
  templateUrl: './serveur-detail.component.html'
})
export class ServeurDetailComponent implements OnInit {
  serveur: IServeur;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected civiliteService: CiviliteService,
    protected jhiAlertService: JhiAlertService
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ serveur }) => {
      this.serveur = serveur;

      this.getCiviliteName(serveur.civiliteId);
    });
  }

  getCiviliteName(id) {
    this.civiliteService
      .find(id)
      .pipe(map((response: HttpResponse<ICivilite>) => response.body))
      .subscribe((res: ICivilite) => (this.serveur.civiliteName = res.libelle), (res: HttpErrorResponse) => this.onError(res.message));
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  previousState() {
    window.history.back();
  }
}
