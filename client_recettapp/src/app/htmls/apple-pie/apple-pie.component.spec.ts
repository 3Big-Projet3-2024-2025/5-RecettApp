import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplePieComponent } from './apple-pie.component';

describe('ApplePieComponent', () => {
  let component: ApplePieComponent;
  let fixture: ComponentFixture<ApplePieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplePieComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ApplePieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
