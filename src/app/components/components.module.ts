import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CardComponent } from './card/card.component';
import { MatCardModule } from '@angular/material/card';
import { ComponentsComponent } from './components.component';
@NgModule({
  declarations: [
    ComponentsComponent,
  ],
  imports: [
    CommonModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule
  ],
  exports: [
    MatButtonModule,
    MatInputModule,
    MatCardModule
  ]
})
export class ComponentsModule { }
