import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Recipe } from '../models/recipe';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { CommonModule } from '@angular/common';
import { RecipeComponentService } from '../services/recipe_Service/recipe-component.service';
import { ImageServiceService } from '../services/image-service.service';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { EvaluationComponent } from "../evaluation/evaluation.component";
import { Evaluation } from '../models/evaluation';
import { EvaluationService } from '../services/evaluation.service';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.css'
})
export class RecipeDetailComponent implements OnInit {
  recipe?: Recipe;
  imageRecipe: string | null = null;
  evaluation: Evaluation = {
    id: 0,
    rate: 0
  };
  imageFile: File | null = null;
  imageError: string | null = null;
  previewImage = "./assets/No_Image.png";

  constructor(private route: ActivatedRoute,private router: Router, private service: RecipeService, private serviceRecipeComponent: RecipeComponentService
              ,private imaService: ImageServiceService, private evaluationService : EvaluationService, private imService : ImageServiceService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.getRecipeById(+id).subscribe(
        (data) => {
        if (data.photo_url) {
          this.getImage(data.photo_url);
        }
        this.getRecipeComponent(+id);
        this.recipe = data;
        this.evaluation.entry = this.recipe.entry;
        this.evaluation.recipe = this.recipe;
        },(err) => {
          console.log(err.error.message)
        }

      );}

  }

  getImage(imageName: string){
    this.imaService.getImage(imageName).subscribe(
      (next: Blob) => {

        this.imageRecipe = URL.createObjectURL(next);
      },
      (err) => {
       console.log(err.error.message)
      }
    );
  }
  getRecipeComponent(id : number){
    this.serviceRecipeComponent.getRecipeComponentsByIdRecipe(+id).subscribe(
      (data) => {
      if (this.recipe) {
        this.recipe.components = data;
      }
    }, (err) => {
      console.log(err.error.message)
     }
    );
  }

  backRecipeList(): void {
    this.router.navigate(['/recipe']);
  }

  addImage(evaluation : Evaluation){

    if(this.imageFile){ //added image
      this.imService.addImageEvaluation(this.imageFile,evaluation).subscribe( // add image before the recipe
        { next: () =>{
          console.log("Image added");
        },
          error: (err) => {
            console.log(err)
          }
        })
      }
  }

  addEvaluation(): void {
    this.evaluationService.addEvaluation(this.evaluation).subscribe(
      (next: Evaluation) => {
        console.log("id retourne : ", next.id);
        this.addImage(next);

        Swal.fire({
          title: 'Évaluation ajoutée !',
          text: 'Votre évaluation a été ajoutée avec succès !',
          icon: 'success',
          confirmButtonText: 'Fermer'
        });

        console.log("Added");
      },
      (err) => {
        console.log(err);
      }
    );
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
