import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailableContestComponent } from './available-contest.component';

describe('AvailableContestComponent', () => {
  let component: AvailableContestComponent;
  let fixture: ComponentFixture<AvailableContestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvailableContestComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AvailableContestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
