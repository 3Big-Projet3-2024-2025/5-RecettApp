import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserRecipeListComponent } from './user-recipe-list.component';

describe('UserRecipeListComponent', () => {
  let component: UserRecipeListComponent;
  let fixture: ComponentFixture<UserRecipeListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserRecipeListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserRecipeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
