import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Plat } from 'app/shared/model/plat.model';
import { PlatService } from './plat.service';
import { PlatComponent } from './plat.component';
import { PlatDetailComponent } from './plat-detail.component';
import { PlatUpdateComponent } from './plat-update.component';
import { PlatDeletePopupComponent } from './plat-delete-dialog.component';
import { IPlat } from 'app/shared/model/plat.model';

@Injectable({ providedIn: 'root' })
export class PlatResolve implements Resolve<IPlat> {
  constructor(private service: PlatService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlat> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Plat>) => response.ok),
        map((plat: HttpResponse<Plat>) => plat.body)
      );
    }
    return of(new Plat());
  }
}

export const platRoute: Routes = [
  {
    path: '',
    component: PlatComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'palmierDevApp.plat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlatDetailComponent,
    resolve: {
      plat: PlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.plat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlatUpdateComponent,
    resolve: {
      plat: PlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.plat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlatUpdateComponent,
    resolve: {
      plat: PlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.plat.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const platPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlatDeletePopupComponent,
    resolve: {
      plat: PlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.plat.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
