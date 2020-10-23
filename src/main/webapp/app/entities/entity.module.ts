import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'serveur',
        loadChildren: () => import('./serveur/serveur.module').then(m => m.PalmierDevServeurModule)
      },
      {
        path: 'plat',
        loadChildren: () => import('./plat/plat.module').then(m => m.PalmierDevPlatModule)
      },
      {
        path: 'boisson',
        loadChildren: () => import('./boisson/boisson.module').then(m => m.PalmierDevBoissonModule)
      },
      {
        path: 'commande',
        loadChildren: () => import('./commande/commande.module').then(m => m.PalmierDevCommandeModule)
      },
      {
        path: 'type-plat',
        loadChildren: () => import('./type-plat/type-plat.module').then(m => m.PalmierDevTypePlatModule)
      },
      {
        path: 'type-boisson',
        loadChildren: () => import('./type-boisson/type-boisson.module').then(m => m.PalmierDevTypeBoissonModule)
      },
      {
        path: 'civilite',
        loadChildren: () => import('./civilite/civilite.module').then(m => m.PalmierDevCiviliteModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PalmierDevEntityModule {}
