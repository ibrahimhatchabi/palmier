import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICivilite } from 'app/shared/model/civilite.model';

@Component({
  selector: 'jhi-civilite-detail',
  templateUrl: './civilite-detail.component.html'
})
export class CiviliteDetailComponent implements OnInit {
  civilite: ICivilite;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ civilite }) => {
      this.civilite = civilite;
    });
  }

  previousState() {
    window.history.back();
  }
}
