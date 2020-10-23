import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeBoisson } from 'app/shared/model/type-boisson.model';

@Component({
  selector: 'jhi-type-boisson-detail',
  templateUrl: './type-boisson-detail.component.html'
})
export class TypeBoissonDetailComponent implements OnInit {
  typeBoisson: ITypeBoisson;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typeBoisson }) => {
      this.typeBoisson = typeBoisson;
    });
  }

  previousState() {
    window.history.back();
  }
}
