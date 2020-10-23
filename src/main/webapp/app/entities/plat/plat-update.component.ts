import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlat, Plat } from 'app/shared/model/plat.model';
import { PlatService } from './plat.service';
import { ITypePlat } from 'app/shared/model/type-plat.model';
import { TypePlatService } from 'app/entities/type-plat/type-plat.service';
import { ICommande } from 'app/shared/model/commande.model';
import { CommandeService } from 'app/entities/commande/commande.service';

@Component({
  selector: 'jhi-plat-update',
  templateUrl: './plat-update.component.html'
})
export class PlatUpdateComponent implements OnInit {
  isSaving: boolean;

  typeplats: ITypePlat[];

  commandes: ICommande[];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    code: [],
    typeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected platService: PlatService,
    protected typePlatService: TypePlatService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ plat }) => {
      this.updateForm(plat);
    });
    this.typePlatService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITypePlat[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITypePlat[]>) => response.body)
      )
      .subscribe((res: ITypePlat[]) => (this.typeplats = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.commandeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICommande[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommande[]>) => response.body)
      )
      .subscribe((res: ICommande[]) => (this.commandes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(plat: IPlat) {
    this.editForm.patchValue({
      id: plat.id,
      libelle: plat.libelle,
      code: plat.code,
      typeId: plat.typeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const plat = this.createFromForm();
    if (plat.id !== undefined) {
      this.subscribeToSaveResponse(this.platService.update(plat));
    } else {
      this.subscribeToSaveResponse(this.platService.create(plat));
    }
  }

  private createFromForm(): IPlat {
    return {
      ...new Plat(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value,
      code: this.editForm.get(['code']).value,
      typeId: this.editForm.get(['typeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlat>>) {
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

  trackTypePlatById(index: number, item: ITypePlat) {
    return item.id;
  }

  trackCommandeById(index: number, item: ICommande) {
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
