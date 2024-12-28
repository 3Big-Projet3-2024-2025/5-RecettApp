import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from '../models/recipe';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { IngredientComponent } from "../ingredient/ingredient.component";
import { RecipeComponentService } from '../services/recipe_Service/recipe-component.service';
import { RecipeComponent } from '../models/recipe-component';
import { ImageServiceService } from '../services/image-service.service';
import { Entry } from '../models/entry';
import { KeycloakService } from 'keycloak-angular';
import { EntriesService } from '../services/entries.service';
import { User } from '../models/users';
import { UsersService } from '../services/users.service';
import {jwtDecode} from 'jwt-decode';


@Component({
  selector: 'app-add-recipe',
  standalone: true,
  imports: [CommonModule, FormsModule, IngredientComponent],
  templateUrl: './add-recipe.component.html',
  styleUrl: './add-recipe.component.css'
})
export class AddRecipeComponent {

  constructor(private route: ActivatedRoute,private recipeService: RecipeService, private componentService: RecipeComponentService,
              private router: Router,private imService: ImageServiceService,private authService: KeycloakService,private entryService: EntriesService,
              private userService: UsersService){}
      
  //Init image variables
  imageFile: File | null = null; 
  imageError: string | null = null;

  // Default preview image when no image is uploaded
  previewImage = "./assets/No_Image.png";

  // Object representing the recipe being created or added
  recipeToAdd: Recipe = {
    id: 0,
    title: '',
    description: '',
    category: '',
    preparation_time: 0,
    cooking_time: 0,
    servings: 0,
    difficulty_level: '',
    instructions: '',
    approved: false,
    photo_url: '',
    entry: undefined,
    components: [],
    recipe_type: undefined,
    image: []
  };
  recipeComponentError = "";
            
  ngOnInit(): void {
    const entryId = this.route.snapshot.paramMap.get('idEntry');
    if (entryId != null) {
      this.entryService.getEntry(+entryId).subscribe({
        next: (entry) => {
          this.recipeToAdd.entry = entry;
          if (entry.users) {
            this.checkUserIdentity(entry.users);
          }else{
            console.log("User in entry can't be null");
            this.router.navigate(['/recipe']);
          }
        },
        error: (error) => {
            console.log(error.error.message);
            this.router.navigate(['/recipe']);
        }
      });
    }
  }

  async checkUserIdentity(user: User) {
    try {
      const token = await this.authService.getToken();
      const decodedToken: any = jwtDecode(token);
  
      // Ensure the necessary fields exist in the decoded token
      if (!decodedToken || !decodedToken.email || !decodedToken.given_name || !decodedToken.family_name) {
        console.log("Invalid token structure. Missing necessary user information.");
        return;
      }

      // Check if the user from the entry matches the authenticated user
      const isAuthorized =
        user.email === decodedToken.email &&
        user.firstName === decodedToken.given_name &&
        user.lastName === decodedToken.family_name;
      if (!isAuthorized) {
        console.log("You do not have access to this contest.");
        this.router.navigate(['/recipe']); 
      }

    } catch (error) {
      console.error("Error decoding the token:", error);
      this.router.navigate(['/recipe']); 
    }
  }
  
  onSubmit(): void {
    if (this.checkValidRecipeToAdd(this.recipeToAdd)) {
     
      if (this.imageFile) {
        this.creatImageData()
      }
        this.recipeService.addRecipe(this.recipeToAdd).subscribe(
          {
            next: (value: Recipe) => {
              this.addImage(value);
            },
            error: (err) => {
              console.log(err.error.message)
            },
          }
        )//console.log(this.recipeToAdd)
      } 
  }

  creatImageData(){
    if (this.imageFile) {
      const newFileName = `${this.recipeToAdd.title.replace(/[\s:]+/g, '_')}_${Date.now()}.${this.imageFile.type.split('/')[1]}`;
      this.recipeToAdd.photo_url= newFileName;

      // Set the new file
      this.imageFile = new File([this.imageFile], newFileName, { type: this.imageFile.type });
      ;
    }
  }
  onRecipeComponentsChange(components: any[]) {
    this.recipeToAdd.components = components;
  }

  addImage(recipe: Recipe){
    this.recipeToAdd.id = recipe.id; // Recovery of the recipe updated with its new id
    this.addRecipeComponent();

    if(this.imageFile){ //added image
      this.imService.addImage(this.imageFile,this.recipeToAdd).subscribe( // add image before the recipe
        { next: () =>{
          this.router.navigate(['/recipe', recipe.id]);
        },
          error: (err) => {
            console.log(err)
          }
        })
      }
  }
  addRecipeComponent(){
    this.recipeToAdd.components.forEach(element => {
      
     // element.recipe = this.recipeToAdd // Recovery of the recipe updated with its new id
        this.componentService.addRecipeComponents(this.creatComponent(element)).subscribe({
          error: (err) => {
            console.log(err.error.message)
          }
        })
    });
  }

  creatComponent( element: RecipeComponent): RecipeComponent{
    const componentToAdd: RecipeComponent = {
      id: element.id,
      recipe: this.recipeToAdd,
      quantity: element.quantity,
      ingredient: element.ingredient, 
      unit: element.unit
    }

    return componentToAdd
  }

  checkValidRecipeToAdd(recipe: Recipe): boolean {
    if (!recipe.components || recipe.components.length === 0) {
      this.recipeComponentError = "Recipe must have at least one ingredient";
      this.imageError ="";
      return false;
    }
    if (!this.imageFile) {
      this.imageError = 'No file selected. Please upload an image.';
      this.recipeComponentError = '';
      return false;
    }
    return true;
  }

  
  validateImage(event: Event) {
    
    const input = event.target as HTMLInputElement;

    if (input && input.files && input.files.length > 0) {
      const file: File = input.files[0];
      const validTypes = ['image/jpeg', 'image/png', 'image/svg+xml', 'image/webp'];
      const maxSize = 5 * 1024 * 1024; //image maxSize

      if (!validTypes.includes(file.type)) {
        this.imageError = 'Invalid file type. Please upload an image (JPEG, PNG, SVG, or WEBP).';
        this.imageFile = null; 
        input.value = ''; 
      } else if (file.size > maxSize) {
        this.imageError = `File size exceeds the limit of 5 MB. Please upload a smaller file.`;
        this.imageFile = null; 
        input.value = ''; 
      } else {
        this.imageError = null;
        this.imageFile = file;
        this.previewImage = URL.createObjectURL(file); 
      }
    } else {
      this.imageError = 'No file selected. Please upload an image.';


    }
  }
}
