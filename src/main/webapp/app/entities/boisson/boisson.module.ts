import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { BoissonComponent } from './boisson.component';
import { BoissonDetailComponent } from './boisson-detail.component';
import { BoissonUpdateComponent } from './boisson-update.component';
import { BoissonDeletePopupComponent, BoissonDeleteDialogComponent } from './boisson-delete-dialog.component';
import { boissonRoute, boissonPopupRoute } from './boisson.route';

const ENTITY_STATES = [...boissonRoute, ...boissonPopupRoute];

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BoissonComponent,
    BoissonDetailComponent,
    BoissonUpdateComponent,
    BoissonDeleteDialogComponent,
    BoissonDeletePopupComponent
  ],
  entryComponents: [BoissonDeleteDialogComponent]
})
export class PalmierDevBoissonModule {}
