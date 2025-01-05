import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserContestComponent } from './user-contest.component';

describe('UserContestComponent', () => {
  let component: UserContestComponent;
  let fixture: ComponentFixture<UserContestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserContestComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserContestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
