import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChickenBreastComponent } from './chicken-breast.component';

describe('ChickenBreastComponent', () => {
  let component: ChickenBreastComponent;
  let fixture: ComponentFixture<ChickenBreastComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChickenBreastComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChickenBreastComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
