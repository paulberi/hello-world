const grid = document.querySelector(".board");
const input = document.getElementById("numberOfGrids");
const resetButton = document.querySelector(".reset");

function createGrid() {
    for (let i = 0; i < 256; i++) {
        const div = document.createElement("div");
        div.classList.add("square");
        grid.appendChild(div);
    }
}

function updateGrid() {
    grid.innerHTML = "";
    grid.style.setProperty("grid-template-columns",
        `repeat(${input.value}, 2fr)`);
    grid.style.setProperty("grid-template-rows",
        `repeat(${input.value}, 2fr)`);
    for (let i = 0; i < input.value * input.value; i++) {
        const div = document.createElement("div");
        div.classList.add("square");
        grid.appendChild(div);
    }
    console.log(input.value);
}
const square = document.querySelector("div");
square.addEventListener("mouseover", function(event) {
    event.target.classList.replace("square", "color");
});
input.addEventListener("change", updateGrid);
resetButton.addEventListener("click", function() {
    grid.innerHTML = "";
    input.value = "";
    grid.style.setProperty("grid-template-columns", `repeat(16,2fr)`);
    grid.style.setProperty("grid-template-rows", `repeat(16, 2fr)`);
    createGrid();
});
loaderButton.addEventListener("click", function() {
    updateGrid();
});

function cursorRight(cell) {
    cell.nextElementSibling.focus();
}

function cursorLeft(cell) {
    cell.nextElementSibling.focus();
}

function cursorUp(cell) {
    cell.nextElementSibling.focus();
}

function cursorDown(cell) {
    cell.nextElementSibling.focus();
}

document.addEventListener("keyup", function(event) {
    if (event.key === "ArrowRight") {
        cursorRight(event.target);
    }
    if (event.key === "ArrowLeft") {
        cursorLeft(event.target);
    }
    if (event.key === "ArrowUp") {
        cursorUp(event.target);
    }
    if (event.key === "ArrowDown") {
        cursorDown(event.target);
    }
})