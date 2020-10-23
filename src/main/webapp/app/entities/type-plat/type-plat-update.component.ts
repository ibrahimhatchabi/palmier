import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ITypePlat, TypePlat } from 'app/shared/model/type-plat.model';
import { TypePlatService } from './type-plat.service';

@Component({
  selector: 'jhi-type-plat-update',
  templateUrl: './type-plat-update.component.html'
})
export class TypePlatUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [],
    libelle: []
  });

  constructor(protected typePlatService: TypePlatService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ typePlat }) => {
      this.updateForm(typePlat);
    });
  }

  updateForm(typePlat: ITypePlat) {
    this.editForm.patchValue({
      id: typePlat.id,
      code: typePlat.code,
      libelle: typePlat.libelle
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const typePlat = this.createFromForm();
    if (typePlat.id !== undefined) {
      this.subscribeToSaveResponse(this.typePlatService.update(typePlat));
    } else {
      this.subscribeToSaveResponse(this.typePlatService.create(typePlat));
    }
  }

  private createFromForm(): ITypePlat {
    return {
      ...new TypePlat(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePlat>>) {
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
