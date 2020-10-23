import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TypeBoisson } from 'app/shared/model/type-boisson.model';
import { TypeBoissonService } from './type-boisson.service';
import { TypeBoissonComponent } from './type-boisson.component';
import { TypeBoissonDetailComponent } from './type-boisson-detail.component';
import { TypeBoissonUpdateComponent } from './type-boisson-update.component';
import { TypeBoissonDeletePopupComponent } from './type-boisson-delete-dialog.component';
import { ITypeBoisson } from 'app/shared/model/type-boisson.model';

@Injectable({ providedIn: 'root' })
export class TypeBoissonResolve implements Resolve<ITypeBoisson> {
  constructor(private service: TypeBoissonService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITypeBoisson> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TypeBoisson>) => response.ok),
        map((typeBoisson: HttpResponse<TypeBoisson>) => typeBoisson.body)
      );
    }
    return of(new TypeBoisson());
  }
}

export const typeBoissonRoute: Routes = [
  {
    path: '',
    component: TypeBoissonComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'palmierDevApp.typeBoisson.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TypeBoissonDetailComponent,
    resolve: {
      typeBoisson: TypeBoissonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typeBoisson.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TypeBoissonUpdateComponent,
    resolve: {
      typeBoisson: TypeBoissonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typeBoisson.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TypeBoissonUpdateComponent,
    resolve: {
      typeBoisson: TypeBoissonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typeBoisson.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const typeBoissonPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TypeBoissonDeletePopupComponent,
    resolve: {
      typeBoisson: TypeBoissonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'palmierDevApp.typeBoisson.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
