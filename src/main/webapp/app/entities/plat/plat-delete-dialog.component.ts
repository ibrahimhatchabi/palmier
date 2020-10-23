import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlat } from 'app/shared/model/plat.model';
import { PlatService } from './plat.service';

@Component({
  selector: 'jhi-plat-delete-dialog',
  templateUrl: './plat-delete-dialog.component.html'
})
export class PlatDeleteDialogComponent {
  plat: IPlat;

  constructor(protected platService: PlatService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.platService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'platListModification',
        content: 'Deleted an plat'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-plat-delete-popup',
  template: ''
})
export class PlatDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ plat }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlatDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.plat = plat;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/plat', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/plat', { outlets: { popup: null } }]);
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
