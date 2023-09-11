import {ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { Todo } from 'src/app/models/Todo';

import { TodosComponent } from './todos.component';

describe('TodosComponent', () => {
  let component: TodosComponent;
  let fixture: ComponentFixture<TodosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TodosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TodosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should delete', ()=>{
    expect(component.deleteTodo).toHaveBeenCalled;
    expect(component).toBeTruthy();
  });

  it('should create', () => {
    expect(component.addTodo).toHaveBeenCalled;
    expect(component).toBeTruthy();
  });
  it("calls function deleteTodo", () => {
    fixture.detectChanges();
    let buttonElement=fixture.debugElement.nativeElement.querySelector('.Remove-button');
    spyOn(component, 'deleteTodo').and.callThrough();//spyon works for class then method as spyOn(class,'method')
    buttonElement.triggerEventHandler('click',null);
    tick();
    expect(component.deleteTodo).toHaveBeenCalled;
    
  });
  it("calls function deleteTodo1", () => {
    var todo= spyOn(component,'deleteTodo');
    component.deleteTodo(0);
    expect(todo).toHaveBeenCalled();

  });
});

