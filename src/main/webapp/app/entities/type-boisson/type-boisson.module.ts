import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { TypeBoissonComponent } from './type-boisson.component';
import { TypeBoissonDetailComponent } from './type-boisson-detail.component';
import { TypeBoissonUpdateComponent } from './type-boisson-update.component';
import { TypeBoissonDeletePopupComponent, TypeBoissonDeleteDialogComponent } from './type-boisson-delete-dialog.component';
import { typeBoissonRoute, typeBoissonPopupRoute } from './type-boisson.route';

const ENTITY_STATES = [...typeBoissonRoute, ...typeBoissonPopupRoute];

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TypeBoissonComponent,
    TypeBoissonDetailComponent,
    TypeBoissonUpdateComponent,
    TypeBoissonDeleteDialogComponent,
    TypeBoissonDeletePopupComponent
  ],
  entryComponents: [TypeBoissonDeleteDialogComponent]
})
export class PalmierDevTypeBoissonModule {}
