import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICivilite, Civilite } from 'app/shared/model/civilite.model';
import { CiviliteService } from './civilite.service';

@Component({
  selector: 'jhi-civilite-update',
  templateUrl: './civilite-update.component.html'
})
export class CiviliteUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [],
    libelle: []
  });

  constructor(protected civiliteService: CiviliteService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ civilite }) => {
      this.updateForm(civilite);
    });
  }

  updateForm(civilite: ICivilite) {
    this.editForm.patchValue({
      id: civilite.id,
      code: civilite.code,
      libelle: civilite.libelle
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const civilite = this.createFromForm();
    if (civilite.id !== undefined) {
      this.subscribeToSaveResponse(this.civiliteService.update(civilite));
    } else {
      this.subscribeToSaveResponse(this.civiliteService.create(civilite));
    }
  }

  private createFromForm(): ICivilite {
    return {
      ...new Civilite(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICivilite>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
