import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TypePlat } from 'app/shared/model/type-plat.model';
import { TypePlatService } from './type-plat.service';
import { TypePlatComponent } from './type-plat.component';
import { TypePlatDetailComponent } from './type-plat-detail.component';
import { TypePlatUpdateComponent } from './type-plat-update.component';
import { TypePlatDeletePopupComponent } from './type-plat-delete-dialog.component';
import { ITypePlat } from 'app/shared/model/type-plat.model';

@Injectable({ providedIn: 'root' })
export class TypePlatResolve implements Resolve<ITypePlat> {
  constructor(private service: TypePlatService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITypePlat> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TypePlat>) => response.ok),
        map((typePlat: HttpResponse<TypePlat>) => typePlat.body)
      );
    }
    return of(new TypePlat());
  }
}

export const typePlatRoute: Routes = [
  {
    path: '',
    component: TypePlatComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'palmierDevApp.typePlat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TypePlatDetailComponent,
    resolve: {
      typePlat: TypePlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typePlat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TypePlatUpdateComponent,
    resolve: {
      typePlat: TypePlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typePlat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TypePlatUpdateComponent,
    resolve: {
      typePlat: TypePlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typePlat.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const typePlatPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TypePlatDeletePopupComponent,
    resolve: {
      typePlat: TypePlatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typePlat.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
