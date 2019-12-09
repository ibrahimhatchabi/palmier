import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PalmierDevSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [PalmierDevSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class PalmierDevHomeModule {}
