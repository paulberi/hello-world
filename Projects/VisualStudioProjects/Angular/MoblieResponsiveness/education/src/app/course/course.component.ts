import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  course=[
    { 'id':1,'name':'JQUERRY','description':'Lorem ipsum Documentation and examples for Bootstrap’s powerful, responsive navigation header, the navbar. Includes support for branding, navigation, and more, including support for our collapse plugin', 'image':'../../assets/download 3.png' },
    { 'id':2,'name':'Angular','description':'Lorem ipsum Documentation and examples for Bootstrap’s powerful, responsive navigation header, the navbar. Includes support for branding, navigation, and more, including support for our collapse plugin', 'image':'../../assets/download 1.png' },
    { 'id':3,'name':'Java','description':'Lorem ipsum Documentation and examples for Bootstrap’s powerful, responsive navigation header, the navbar. Includes support for branding, navigation, and more, including support for our collapse plugin', 'image':'../../assets/download.png' },
    { 'id':4,'name':'NodeJS','description':'Lorem ipsum Documentation and examples for Bootstrap’s powerful, responsive navigation header, the navbar. Includes support for branding, navigation, and more, including support for our collapse plugin' , 'image':'../../assets/download 2.png' }

  ]

}
