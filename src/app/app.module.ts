import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './Login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NewRegistrationComponent } from './dashboard/new-registration/new-registration.component';
import { ButtonComponent } from './components/button/button.component';
import { TextFieldComponent } from './components/text-field/text-field.component';
import { CardComponent } from './components/card/card.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReactiveFormsModule } from '@angular/forms';
import { ComponentsModule } from './components/components.module';
import { AuthGuard } from './_helpers/auth.guard';
import { AuthService } from './_helpers/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './header/header.component';
import { SubSink } from 'subsink';
import { RegistryViewDetailsComponent } from './dashboard/registry-view-details/registry-view-details.component';
import { PropertyViewDetailsComponent } from './dashboard/property-view-details/property-view-details.component';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SetPasswordComponent } from './set-password/set-password.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatChipsModule } from '@angular/material/chips';
import { MyWorkflowComponent } from './dashboard/my-workflow/my-workflow.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {MatExpansionModule} from '@angular/material/expansion';
import { BuyPropertyComponent } from './dashboard/buy-property/buy-property.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    NewRegistrationComponent,
    ButtonComponent,
    TextFieldComponent,
    CardComponent,
    HeaderComponent,
    RegistryViewDetailsComponent,
    PropertyViewDetailsComponent,
    SetPasswordComponent,
    MyWorkflowComponent,
    BuyPropertyComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    ComponentsModule,
    FlexLayoutModule,
    HttpClientModule,
    MatIconModule,
    MatTooltipModule,
    MatStepperModule,
    MatSidenavModule,
    MatChipsModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatIconModule

  ],
  providers: [AuthService, AuthGuard, SubSink],
  bootstrap: [AppComponent]
})
export class AppModule { }
