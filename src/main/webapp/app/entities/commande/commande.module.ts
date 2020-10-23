import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { CommandeComponent } from './commande.component';
import { CommandeDetailComponent } from './commande-detail.component';
import { CommandeUpdateComponent } from './commande-update.component';
import { CommandeDeletePopupComponent, CommandeDeleteDialogComponent } from './commande-delete-dialog.component';
import { commandeRoute, commandePopupRoute } from './commande.route';

const ENTITY_STATES = [...commandeRoute, ...commandePopupRoute];

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CommandeComponent,
    CommandeDetailComponent,
    CommandeUpdateComponent,
    CommandeDeleteDialogComponent,
    CommandeDeletePopupComponent
  ],
  entryComponents: [CommandeDeleteDialogComponent]
})
export class PalmierDevCommandeModule {}
