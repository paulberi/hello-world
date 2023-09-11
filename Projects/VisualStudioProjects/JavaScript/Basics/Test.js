console.log('Hello Guys!');
/**
 * preparation for final presentation
 * $prefix  is used to mean geting or setting variable or item by id.
 * in angular $ prefix means observable
 * 
 * express.urencoded(). built in middleware that parses incoming payloads and is based on body parser, and returns an object
 */

//angular routing

/**
 to import the router module alongside the routes
 RouterModule, forRoot([
     here is an array of roots
     {path: 'path name', component: component name} by this you are telling the browser that when the path changes to name it should display the component

     path: '', when the place of the path name is empty it signfies the default page or home page

     path: '**' a white care which catches any url that is invalid, should be the last path

     with routing you need to put more specific files at the top
 ])
 */

/*
Resources
js library for UI
https://bulma.io/documentation/form/general/

google developer icon fonts
https://material-ui.com/components/material-icons/
https://google.github.io/material-design-icons/


the below has links to angular material.io imports
https://stackoverflow.com/questions/58594311/angular-material-index-d-ts-is-not-a-module

*/
//basic syntax for javascript html
/*
<html>

    <head>
         <title>My title</title>
         <script>
         
         </script>
        //"<h1></h1>"
    </head>

    <body>
    
    </body>

</html>

*/

//naming convention
/***
 * the rules for creating an identifier in Javascript are, the name of the identifier shoud not be any pre defined keyword, the first character must be a letter, an underscore(_) or a dollar sign($). Subsequent characters may be any letter or digit or an underscore or dollar sign. Variables are case sensitive
 */

/*
Datatypes
undefined, null, boolean, string, symbol, number, and object

var:
this stands for variables
var can be also defined using let and const.
const never changes, let is used only where it is disclosed
and var is more universal


*/
var a;
var b = 3;
a = b;
console.log(a);
a = a + 1;
b = b + 5;
var c = "I am a";
c = c + " String"
console.log(c);
var x = 100;
while (x > 0) {
    console.log(x);
    x--;
}
console.log("hello");

for (i = 0; i < 100; i++) {
    if (i % 5 == '0') {
        console.log(i);

    }
}