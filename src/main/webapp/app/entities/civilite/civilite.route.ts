import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Civilite } from 'app/shared/model/civilite.model';
import { CiviliteService } from './civilite.service';
import { CiviliteComponent } from './civilite.component';
import { CiviliteDetailComponent } from './civilite-detail.component';
import { CiviliteUpdateComponent } from './civilite-update.component';
import { CiviliteDeletePopupComponent } from './civilite-delete-dialog.component';
import { ICivilite } from 'app/shared/model/civilite.model';

@Injectable({ providedIn: 'root' })
export class CiviliteResolve implements Resolve<ICivilite> {
  constructor(private service: CiviliteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICivilite> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Civilite>) => response.ok),
        map((civilite: HttpResponse<Civilite>) => civilite.body)
      );
    }
    return of(new Civilite());
  }
}

export const civiliteRoute: Routes = [
  {
    path: '',
    component: CiviliteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'palmierDevApp.civilite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CiviliteDetailComponent,
    resolve: {
      civilite: CiviliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.civilite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CiviliteUpdateComponent,
    resolve: {
      civilite: CiviliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.civilite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CiviliteUpdateComponent,
    resolve: {
      civilite: CiviliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.civilite.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const civilitePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CiviliteDeletePopupComponent,
    resolve: {
      civilite: CiviliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.civilite.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
