import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IServeur, Serveur } from 'app/shared/model/serveur.model';
import { ServeurService } from './serveur.service';
import { ICivilite } from 'app/shared/model/civilite.model';
import { CiviliteService } from 'app/entities/civilite/civilite.service';

@Component({
  selector: 'jhi-serveur-update',
  templateUrl: './serveur-update.component.html'
})
export class ServeurUpdateComponent implements OnInit {
  isSaving: boolean;

  civilites: ICivilite[];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    telephone: [],
    civiliteId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected serveurService: ServeurService,
    protected civiliteService: CiviliteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ serveur }) => {
      this.updateForm(serveur);
    });
    this.civiliteService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICivilite[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICivilite[]>) => response.body)
      )
      .subscribe((res: ICivilite[]) => (this.civilites = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(serveur: IServeur) {
    this.editForm.patchValue({
      id: serveur.id,
      nom: serveur.nom,
      prenom: serveur.prenom,
      telephone: serveur.telephone,
      civiliteId: serveur.civiliteId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const serveur = this.createFromForm();
    if (serveur.id !== undefined) {
      this.subscribeToSaveResponse(this.serveurService.update(serveur));
    } else {
      this.subscribeToSaveResponse(this.serveurService.create(serveur));
    }
  }

  private createFromForm(): IServeur {
    return {
      ...new Serveur(),
      id: this.editForm.get(['id']).value,
      nom: this.editForm.get(['nom']).value,
      prenom: this.editForm.get(['prenom']).value,
      telephone: this.editForm.get(['telephone']).value,
      civiliteId: this.editForm.get(['civiliteId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServeur>>) {
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

  trackCiviliteById(index: number, item: ICivilite) {
    return item.id;
  }
}
