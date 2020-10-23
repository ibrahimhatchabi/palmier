import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITypeBoisson } from 'app/shared/model/type-boisson.model';
import { TypeBoissonService } from './type-boisson.service';

@Component({
  selector: 'jhi-type-boisson-delete-dialog',
  templateUrl: './type-boisson-delete-dialog.component.html'
})
export class TypeBoissonDeleteDialogComponent {
  typeBoisson: ITypeBoisson;

  constructor(
    protected typeBoissonService: TypeBoissonService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.typeBoissonService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'typeBoissonListModification',
        content: 'Deleted an typeBoisson'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-type-boisson-delete-popup',
  template: ''
})
export class TypeBoissonDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typeBoisson }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TypeBoissonDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.typeBoisson = typeBoisson;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/type-boisson', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/type-boisson', { outlets: { popup: null } }]);
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
