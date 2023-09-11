var key = {
    UP: 38,
    DOWN: 40,
    LEFT: 37,
    RIGHT: 39,
    C: 67
};

console.log(key);

document.addEventListener("keydown", drawAction);
document.addEventListener("keydown", clearCanvas);

var square = document.getElementById("drawPlace");
var ctx = square.getContext("2d");
var x = 100;
var y = 100;

drawing_line("#FF6347", x - 1, y, x, y, ctx);

function drawing_line(color, x_start, y_start, x_end, y_end, board) {
    board.beginPath();
    board.strokeStyle = color;
    board.lineWidth = 2;
    board.moveTo(x_start, y_start);
    board.lineTo(x_end, y_end);
    board.stroke();
    board.closePath();
}

function drawAction(whenPressKey) {

    var colorLine = "#9ACD32";
    var dotMove = 3;

    switch (whenPressKey.keyCode) {

        case key.UP:
            drawing_line(colorLine, x, y, x, y - dotMove, ctx);
            y = y - dotMove;
            break;

        case key.DOWN:
            drawing_line(colorLine, x, y, x, y + dotMove, ctx);
            y = y + dotMove;
            break;

        case key.LEFT:
            drawing_line(colorLine, x, y, x - dotMove, y, ctx);
            x = x - dotMove;
            break;

        case key.RIGHT:
            drawing_line(colorLine, x, y, x + dotMove, y, ctx);
            x = x + dotMove;
            break;

        default:
            console.log("Another key");
    }
}

function clearCanvas(whenPressKey) {
    if (whenPressKey.keyCode == key.C) {
        ctx.clearRect(0, 0, square.width, square.height);
    }


}