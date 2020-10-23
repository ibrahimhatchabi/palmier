import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICivilite } from 'app/shared/model/civilite.model';
import { CiviliteService } from './civilite.service';

@Component({
  selector: 'jhi-civilite-delete-dialog',
  templateUrl: './civilite-delete-dialog.component.html'
})
export class CiviliteDeleteDialogComponent {
  civilite: ICivilite;

  constructor(protected civiliteService: CiviliteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.civiliteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'civiliteListModification',
        content: 'Deleted an civilite'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-civilite-delete-popup',
  template: ''
})
export class CiviliteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ civilite }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CiviliteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.civilite = civilite;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/civilite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/civilite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
