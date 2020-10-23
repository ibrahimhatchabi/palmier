import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBoisson } from 'app/shared/model/boisson.model';
import { BoissonService } from './boisson.service';

@Component({
  selector: 'jhi-boisson-delete-dialog',
  templateUrl: './boisson-delete-dialog.component.html'
})
export class BoissonDeleteDialogComponent {
  boisson: IBoisson;

  constructor(protected boissonService: BoissonService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.boissonService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'boissonListModification',
        content: 'Deleted an boisson'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-boisson-delete-popup',
  template: ''
})
export class BoissonDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ boisson }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BoissonDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.boisson = boisson;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/boisson', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/boisson', { outlets: { popup: null } }]);
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
