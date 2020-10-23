import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { CiviliteComponent } from './civilite.component';
import { CiviliteDetailComponent } from './civilite-detail.component';
import { CiviliteUpdateComponent } from './civilite-update.component';
import { CiviliteDeletePopupComponent, CiviliteDeleteDialogComponent } from './civilite-delete-dialog.component';
import { civiliteRoute, civilitePopupRoute } from './civilite.route';

const ENTITY_STATES = [...civiliteRoute, ...civilitePopupRoute];

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CiviliteComponent,
    CiviliteDetailComponent,
    CiviliteUpdateComponent,
    CiviliteDeleteDialogComponent,
    CiviliteDeletePopupComponent
  ],
  entryComponents: [CiviliteDeleteDialogComponent]
})
export class PalmierDevCiviliteModule {}
