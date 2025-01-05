import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VeganRecipeComponent } from './vegan-recipe.component';

describe('VeganRecipeComponent', () => {
  let component: VeganRecipeComponent;
  let fixture: ComponentFixture<VeganRecipeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VeganRecipeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VeganRecipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
