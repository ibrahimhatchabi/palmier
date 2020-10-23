import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBoisson, Boisson } from 'app/shared/model/boisson.model';
import { BoissonService } from './boisson.service';
import { ITypeBoisson } from 'app/shared/model/type-boisson.model';
import { TypeBoissonService } from 'app/entities/type-boisson/type-boisson.service';
import { ICommande } from 'app/shared/model/commande.model';
import { CommandeService } from 'app/entities/commande/commande.service';

@Component({
  selector: 'jhi-boisson-update',
  templateUrl: './boisson-update.component.html'
})
export class BoissonUpdateComponent implements OnInit {
  isSaving: boolean;

  typeboissons: ITypeBoisson[];

  commandes: ICommande[];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    code: [],
    typeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected boissonService: BoissonService,
    protected typeBoissonService: TypeBoissonService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ boisson }) => {
      this.updateForm(boisson);
    });
    this.typeBoissonService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITypeBoisson[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITypeBoisson[]>) => response.body)
      )
      .subscribe((res: ITypeBoisson[]) => (this.typeboissons = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.commandeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICommande[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommande[]>) => response.body)
      )
      .subscribe((res: ICommande[]) => (this.commandes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(boisson: IBoisson) {
    this.editForm.patchValue({
      id: boisson.id,
      libelle: boisson.libelle,
      code: boisson.code,
      typeId: boisson.typeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const boisson = this.createFromForm();
    if (boisson.id !== undefined) {
      this.subscribeToSaveResponse(this.boissonService.update(boisson));
    } else {
      this.subscribeToSaveResponse(this.boissonService.create(boisson));
    }
  }

  private createFromForm(): IBoisson {
    return {
      ...new Boisson(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value,
      code: this.editForm.get(['code']).value,
      typeId: this.editForm.get(['typeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoisson>>) {
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

  trackTypeBoissonById(index: number, item: ITypeBoisson) {
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
