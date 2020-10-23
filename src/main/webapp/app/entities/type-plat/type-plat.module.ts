import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { TypePlatComponent } from './type-plat.component';
import { TypePlatDetailComponent } from './type-plat-detail.component';
import { TypePlatUpdateComponent } from './type-plat-update.component';
import { TypePlatDeletePopupComponent, TypePlatDeleteDialogComponent } from './type-plat-delete-dialog.component';
import { typePlatRoute, typePlatPopupRoute } from './type-plat.route';

const ENTITY_STATES = [...typePlatRoute, ...typePlatPopupRoute];

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TypePlatComponent,
    TypePlatDetailComponent,
    TypePlatUpdateComponent,
    TypePlatDeleteDialogComponent,
    TypePlatDeletePopupComponent
  ],
  entryComponents: [TypePlatDeleteDialogComponent]
})
export class PalmierDevTypePlatModule {}
