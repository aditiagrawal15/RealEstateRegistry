import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BuyPropertyComponent } from './dashboard/buy-property/buy-property.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MyWorkflowComponent } from './dashboard/my-workflow/my-workflow.component';
import { NewRegistrationComponent } from './dashboard/new-registration/new-registration.component';
import { PropertyViewDetailsComponent } from './dashboard/property-view-details/property-view-details.component';
import { RegistryViewDetailsComponent } from './dashboard/registry-view-details/registry-view-details.component';
import { LoginComponent } from './Login/login.component';
import { SetPasswordComponent } from './set-password/set-password.component';
import { AuthGuard } from './_helpers/auth.guard';

const routes: Routes = [{ path: 'login', component: LoginComponent },
{
  path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]
},
{ path: 'new-registration', component: NewRegistrationComponent },
{
  path: 'registry-view-details', component: RegistryViewDetailsComponent,
},
{
  path: 'property-view-details', component: PropertyViewDetailsComponent,
},
{
  path: 'buy-property', component: BuyPropertyComponent,
},
{
  path: 'my-workflow', component: MyWorkflowComponent,
},
{ path: 'set-password', component: SetPasswordComponent },
{ path: '', pathMatch: 'full', redirectTo: '/login' }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
