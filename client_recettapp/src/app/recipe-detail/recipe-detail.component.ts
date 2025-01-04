import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Recipe } from '../models/recipe';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { CommonModule, Location } from '@angular/common';
import { RecipeComponentService } from '../services/recipe_Service/recipe-component.service';
import { ImageServiceService } from '../services/image-service.service';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { EvaluationComponent } from "../evaluation/evaluation.component";
import { Evaluation } from '../models/evaluation';
import { EvaluationService } from '../services/evaluation.service';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { EntriesService } from '../services/entries.service';


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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: RecipeService,
    private serviceRecipeComponent: RecipeComponentService,
    private imaService: ImageServiceService,
    private evaluationService : EvaluationService,
    private imService : ImageServiceService,
    private location: Location,
    private entriesService : EntriesService

  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.getRecipeById(+id).subscribe(
        (data) => {
        if (data.photo_url) {
          this.getImage(data.photo_url);
        }
        //this.getRecipeComponent(+id);
        this.recipe = data;
        this.getEntry();
        this.evaluation.recipe = this.recipe;
        },(err) => {
          console.log(err.error.message)
        }

      );}

  }


  getEntry() {
    if (this.recipe?.entry?.contest?.id) {
      this.entriesService.getEntryByUserMailAndIdContest(this.recipe?.entry?.contest?.id).subscribe(
        (entrie) => {
          console.log("Recovered input : ", entrie);
          this.evaluation.entry = entrie;
        },
        (err) => {
          console.log("You are not registered for this competition");

          Swal.fire({
            title: 'Not registered',
            text: 'You are not registered for this competition.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      );
    }
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



  backRecipeList(): void {
    this.location.back();
  }

  addImage(evaluation : Evaluation){

    if(this.imageFile){
      this.imService.addImageEvaluation(this.imageFile,evaluation).subscribe(
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
    if (!this.imageFile) {
      Swal.fire({
        title: 'error',
        text: 'Please select an image for your evaluation.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }

    if (!this.evaluation.rate || this.evaluation.rate <= 0) {
      Swal.fire({
        title: 'error',
        text: 'Please give a rating (at least 1 star).',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }

    if (!this.evaluation.commentaire || this.evaluation.commentaire.trim() === '') {
      Swal.fire({
        title: 'Erreur',
        text: 'Please add a comment.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }
    console.log('User who evaluates :', this.evaluation.entry?.users?.id);
    this.evaluationService.addEvaluation(this.evaluation).subscribe(
      (next: Evaluation) => {
        console.log("id retourne : ", next.id);
        this.addImage(next);

        Swal.fire({
          title: 'Valuation added!',
          text: 'Your review has been successfully added!',
          icon: 'success',
          confirmButtonText: 'Close'
        });
      },
      (err) => {
        console.log(err);
        if (err.status === 400) {
          Swal.fire({
            title: 'error',
            text: err.error,
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
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
