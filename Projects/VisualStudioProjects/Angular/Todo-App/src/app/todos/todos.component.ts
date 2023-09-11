import { Component, OnInit } from '@angular/core';
import { Todo } from '../shared 11-54-25-772/todo.model';

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.scss']
})
export class TodosComponent implements OnInit {

  todos: Todo[];
  

  ngOnInit(): void {
  
  }

}
