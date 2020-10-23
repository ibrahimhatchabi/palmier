import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITypePlat } from 'app/shared/model/type-plat.model';
import { TypePlatService } from './type-plat.service';

@Component({
  selector: 'jhi-type-plat-delete-dialog',
  templateUrl: './type-plat-delete-dialog.component.html'
})
export class TypePlatDeleteDialogComponent {
  typePlat: ITypePlat;

  constructor(protected typePlatService: TypePlatService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.typePlatService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'typePlatListModification',
        content: 'Deleted an typePlat'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-type-plat-delete-popup',
  template: ''
})
export class TypePlatDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typePlat }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TypePlatDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.typePlat = typePlat;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/type-plat', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/type-plat', { outlets: { popup: null } }]);
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
