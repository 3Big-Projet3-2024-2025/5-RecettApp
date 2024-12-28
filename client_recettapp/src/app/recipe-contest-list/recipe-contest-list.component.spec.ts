import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeContestListComponent } from './recipe-contest-list.component';

describe('RecipeContestListComponent', () => {
  let component: RecipeContestListComponent;
  let fixture: ComponentFixture<RecipeContestListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeContestListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecipeContestListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
