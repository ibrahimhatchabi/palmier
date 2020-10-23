import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlat } from 'app/shared/model/plat.model';

@Component({
  selector: 'jhi-plat-detail',
  templateUrl: './plat-detail.component.html'
})
export class PlatDetailComponent implements OnInit {
  plat: IPlat;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ plat }) => {
      this.plat = plat;
    });
  }

  previousState() {
    window.history.back();
  }
}
