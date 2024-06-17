import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistryViewDetailsComponent } from './registry-view-details.component';

describe('RegistryViewDetailsComponent', () => {
  let component: RegistryViewDetailsComponent;
  let fixture: ComponentFixture<RegistryViewDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegistryViewDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistryViewDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
