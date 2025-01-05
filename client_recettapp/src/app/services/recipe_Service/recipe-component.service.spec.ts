import { TestBed } from '@angular/core/testing';

import { RecipeComponentService } from './recipe-component.service';

describe('RecipeComponentService', () => {
  let service: RecipeComponentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecipeComponentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
