import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { PlatComponent } from './plat.component';
import { PlatDetailComponent } from './plat-detail.component';
import { PlatUpdateComponent } from './plat-update.component';
import { PlatDeletePopupComponent, PlatDeleteDialogComponent } from './plat-delete-dialog.component';
import { platRoute, platPopupRoute } from './plat.route';

const ENTITY_STATES = [...platRoute, ...platPopupRoute];

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PlatComponent, PlatDetailComponent, PlatUpdateComponent, PlatDeleteDialogComponent, PlatDeletePopupComponent],
  entryComponents: [PlatDeleteDialogComponent]
})
export class PalmierDevPlatModule {}
