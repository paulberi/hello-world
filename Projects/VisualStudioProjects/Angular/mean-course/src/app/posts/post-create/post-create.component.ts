import { Component} from '@angular/core';
import { resetFakeAsyncZone } from '@angular/core/testing';
import { NgForm } from "@angular/forms";


import { PostsService } from '../posts.service';

@Component({
  selector: 'app-post-create',
  templateUrl: './post-create.component.html',
  styleUrls: ['./post-create.component.css']
})

export class PostCreateComponent {

  enteredTitle ="";
  enteredContent="";


  constructor(public postsService: PostsService){}

  onAddPost(form: NgForm){
    if(form.invalid){
      alert('the fields cannot be empty');
      return;
    }
    this.postsService.addPost(form.value.title, form.value.content);
    form.resetForm();
  }
  onUpdatePost(){

  }
  onDeletePost(){
    alert('do u want to delete');
    delete this.postsService

    }
}

