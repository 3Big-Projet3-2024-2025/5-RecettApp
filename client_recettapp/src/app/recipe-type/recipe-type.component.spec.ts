import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeTypeComponent } from './recipe-type.component';

describe('RecipeTypeComponent', () => {
  let component: RecipeTypeComponent;
  let fixture: ComponentFixture<RecipeTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeTypeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecipeTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
