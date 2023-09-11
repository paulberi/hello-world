import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

import {Post} from './post.model';


@Injectable({providedIn: 'root'})
export class PostsService{
  private posts: Post[]=[];
  private PostsUpdated=new Subject<Post[]>();

  getPosts() {
    return this.posts;
   // return[...this.posts];
  }w
  getPostUpdateListener(){
    return this.PostsUpdated.asObservable();
  }
  addPost(title:string, content: string) {
    const post: Post={title:title, content: content};
    this.posts.push(post);
    this.PostsUpdated.next([...this.posts]);
  }

}
