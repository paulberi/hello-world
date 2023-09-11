import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component,  OnDestroy,  OnInit } from '@angular/core';
import {Post } from '../post.model';
import { PostsService } from '../posts.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-post-list',
  templateUrl: 'post-list.component.html',
  styleUrls: ['./post-list.component.css']
})

export class PostListComponent implements OnInit, OnDestroy{

/*
  posts = [
    {title: "First post", content: "This is the first post's content"},
    {title: "second post", content: "This is the second post's content"},
    {title: "thirg post", content: "This is the third post's content"}
  ];*/
  posts: Post[]=[];
  private postsSub: Subscription;

  constructor(public postsService: PostsService) {

  }
  ngOnInit(){
    this.posts=this.postsService.getPosts();
    this.postsSub=this.postsService.getPostUpdateListener().subscribe((posts:Post[])=>{
      this.posts=posts;
    });
  }

ngOnDestroy(){
  this.postsSub.unsubscribe();
}

}
