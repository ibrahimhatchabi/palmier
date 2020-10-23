import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypePlat } from 'app/shared/model/type-plat.model';

@Component({
  selector: 'jhi-type-plat-detail',
  templateUrl: './type-plat-detail.component.html'
})
export class TypePlatDetailComponent implements OnInit {
  typePlat: ITypePlat;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typePlat }) => {
      this.typePlat = typePlat;
    });
  }

  previousState() {
    window.history.back();
  }
}
