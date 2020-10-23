import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ITypeBoisson, TypeBoisson } from 'app/shared/model/type-boisson.model';
import { TypeBoissonService } from './type-boisson.service';

@Component({
  selector: 'jhi-type-boisson-update',
  templateUrl: './type-boisson-update.component.html'
})
export class TypeBoissonUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [],
    libelle: []
  });

  constructor(protected typeBoissonService: TypeBoissonService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ typeBoisson }) => {
      this.updateForm(typeBoisson);
    });
  }

  updateForm(typeBoisson: ITypeBoisson) {
    this.editForm.patchValue({
      id: typeBoisson.id,
      code: typeBoisson.code,
      libelle: typeBoisson.libelle
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const typeBoisson = this.createFromForm();
    if (typeBoisson.id !== undefined) {
      this.subscribeToSaveResponse(this.typeBoissonService.update(typeBoisson));
    } else {
      this.subscribeToSaveResponse(this.typeBoissonService.create(typeBoisson));
    }
  }

  private createFromForm(): ITypeBoisson {
    return {
      ...new TypeBoisson(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeBoisson>>) {
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
