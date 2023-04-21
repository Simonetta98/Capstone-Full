import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';


const routes: Routes = [

  {
    path: '',
    //non possiamom impostare il component della rotta perchè non vogliamo caricarlo finche non ci serve
    //usiamo loadChildren (lazy loading) per caricare un modulo (con il suo routing) solo quando la rotta è raggiunta
    loadChildren: () => {
      return import('src/app/components/home/home.module').then(m => m.HomeModule)
    }
  },
  {
    path: 'profile',
    loadChildren: ()=>{return import('./components/profile/profile.module').then((m)=>{return m.ProfileModule})},
    canActivate: [AuthGuard]
  },
  {
    path: 'layout',
    loadChildren: ()=>{return import('./components/layout/layout.module').then((m)=>{return m.LayoutModule})},
    canActivate: [AuthGuard]
  },
  {
    path: 'details',
    loadChildren: ()=>{return import('./components/details/details.module').then((m)=>{return m.DetailsModule})},
    canActivate: [AuthGuard]
  },

  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
