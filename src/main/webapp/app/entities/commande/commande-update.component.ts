import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICommande, Commande } from 'app/shared/model/commande.model';
import { CommandeService } from './commande.service';
import { IServeur } from 'app/shared/model/serveur.model';
import { ServeurService } from 'app/entities/serveur/serveur.service';
import { IPlat } from 'app/shared/model/plat.model';
import { PlatService } from 'app/entities/plat/plat.service';
import { IBoisson } from 'app/shared/model/boisson.model';
import { BoissonService } from 'app/entities/boisson/boisson.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html'
})
export class CommandeUpdateComponent implements OnInit {
  isSaving: boolean;

  serveurs: IServeur[];

  plats: IPlat[];

  boissons: IBoisson[];

  editForm = this.fb.group({
    id: [],
    numero: [],
    date: [],
    status: [],
    serveurId: [],
    plats: [],
    boissons: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected commandeService: CommandeService,
    protected serveurService: ServeurService,
    protected platService: PlatService,
    protected boissonService: BoissonService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.updateForm(commande);
    });
    this.serveurService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IServeur[]>) => mayBeOk.ok),
        map((response: HttpResponse<IServeur[]>) => response.body)
      )
      .subscribe((res: IServeur[]) => (this.serveurs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.platService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlat[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlat[]>) => response.body)
      )
      .subscribe((res: IPlat[]) => (this.plats = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.boissonService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBoisson[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBoisson[]>) => response.body)
      )
      .subscribe((res: IBoisson[]) => (this.boissons = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(commande: ICommande) {
    this.editForm.patchValue({
      id: commande.id,
      numero: commande.numero,
      date: commande.date != null ? commande.date.format(DATE_TIME_FORMAT) : null,
      status: commande.status,
      serveurId: commande.serveurId,
      plats: commande.plats,
      boissons: commande.boissons
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  private createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id']).value,
      numero: this.editForm.get(['numero']).value,
      // date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      date: moment(moment().unix(), DATE_TIME_FORMAT),
      status: this.editForm.get(['status']).value,
      serveurId: this.editForm.get(['serveurId']).value,
      plats: this.editForm.get(['plats']).value,
      boissons: this.editForm.get(['boissons']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackServeurById(index: number, item: IServeur) {
    return item.id;
  }

  trackPlatById(index: number, item: IPlat) {
    return item.id;
  }

  trackBoissonById(index: number, item: IBoisson) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
