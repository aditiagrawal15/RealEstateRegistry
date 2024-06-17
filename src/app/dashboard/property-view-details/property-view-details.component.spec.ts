import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyViewDetailsComponent } from './property-view-details.component';

describe('PropertyViewDetailsComponent', () => {
  let component: PropertyViewDetailsComponent;
  let fixture: ComponentFixture<PropertyViewDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertyViewDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PropertyViewDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
