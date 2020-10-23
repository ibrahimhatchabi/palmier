import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommande } from 'app/shared/model/commande.model';
import { ServeurService } from 'app/entities/serveur/serveur.service';
import { IServeur } from 'app/shared/model/serveur.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-commande-detail',
  templateUrl: './commande-detail.component.html'
})
export class CommandeDetailComponent implements OnInit {
  commande: ICommande;
  serveur: IServeur = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected serveurService: ServeurService,
    protected jhiAlertService: JhiAlertService
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.commande = commande;
    });
    this.getServeurName(this.commande.serveurId);
  }

  getServeurName(id) {
    this.serveurService
      .find(id)
      .pipe(map((response: HttpResponse<IServeur>) => response.body))
      .subscribe((res: IServeur) => (this.serveur = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  previousState() {
    window.history.back();
  }
}
