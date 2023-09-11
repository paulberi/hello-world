let isDrawing = false;
let x = 0;
let y = 0;

const myPics = document.getElementById("myPics");
const context = myPics.getContext("2d");
const button = document.getElementById("clear");
// event.offsetX, event.offsetY gives the (x,y) offset from the edge of the canvas.

// Add the event listeners for mousedown, mousemove, and mouseup

myPics.addEventListener("mousedown", event => {
    x = event.offsetX;
    y = event.offsetY;
    isDrawing = true;
});
myPics.addEventListener("mousemove", event => {
    if (isDrawing) {
        drawLine(context, x, y, event.offsetX, event.offsetY);
        x = event.offsetX;
        y = event.offsetY;
    }
});
window.addEventListener("mouseup", event => {
    if (isDrawing) {
        drawLine(context, x, y, event.offsetX, event.offsetY);
        x = 0;
        y = 0;
        isDrawing = false;
    }
});

function drawLine(context, x1, y1, x2, y2) {
    context.beginPath();
    context.strokeStyle = "blue";
    context.lineWidth = 1;
    context.moveTo(x1, y1);
    context.lineTo(x2, y2);
    context.stroke();
    context.closePath();
}

function resetScreen() {
    document.getElementById("clear").addEventListener("click", function() {
        context.clearRect(0, 0, canvas.width, canvas.height);
    }, false);

}
button.addEventListener('click', resetScreen);