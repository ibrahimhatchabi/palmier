import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoisson } from 'app/shared/model/boisson.model';
import { map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ITypeBoisson } from 'app/shared/model/type-boisson.model';
import { TypeBoissonService } from 'app/entities/type-boisson/type-boisson.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-boisson-detail',
  templateUrl: './boisson-detail.component.html'
})
export class BoissonDetailComponent implements OnInit {
  boisson: IBoisson;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected typeBoissonService: TypeBoissonService,
    protected jhiAlertService: JhiAlertService
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ boisson }) => {
      this.boisson = boisson;
      this.getTypeBoissonName(boisson.typeId);
    });
  }

  getTypeBoissonName(id) {
    this.typeBoissonService
      .find(id)
      .pipe(map((response: HttpResponse<ITypeBoisson>) => response.body))
      .subscribe((res: ITypeBoisson) => (this.boisson.typeName = res.libelle), (res: HttpErrorResponse) => this.onError(res.message));
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  previousState() {
    window.history.back();
  }
}
