console.log("Connected!");

const canvas = document.querySelector("#etch-a-sketch");
const shakeButton = document.querySelector(`.shake`);
const moveAmount = 10;
const ctx = canvas.getContext("2d");
console.log(ctx)
let hue = 0;
ctx.strokeStyle = `hsl(${hue}, 100%, 50%)`;
const { width, height } = canvas;
let x = Math.floor(Math.random() * width);
let y = Math.floor(Math.random() * height);
ctx.lineJoin = "round";
ctx.linCap = "round";
ctx.lineWidth = 20;
ctx.beginPath();
ctx.moveTo(x, y);
ctx.lineTo(x, y);
ctx.stroke();

function draw({ option }) {
    hue += 10;
    ctx.strokeStyle = `hsl(${hue}, 100%, 50%)`;
    ctx.beginPath();
    ctx.moveTo(x, y);
    switch (option.key) {
        case "ArrowUp":
            y -= moveAmount;
            break;
        case "ArrowRight":
            x += moveAmount;
            break;
        case "ArrowDown":
            y += moveAmount;
            break;
        case "ArrowLeft":
            x -= moveAmount;
            break;
        default:
            break;
    }
    ctx.lineTo(x, y);
    ctx.stroke();
}

function handlekey(event) {
    if (event.key.includes("Arrow")) {
        event.preventDefualt();
        draw({ key: event.key });
    };
}

function clearCanvas() {
    canvas.classList.add("shake");
    ctx.clearRext(0, 0, width, height);
    console.log(`done the shake`);
    canvas.addEventListener("animationend", () => {
        canvas.classList.remove("shake");
    }, { once: true })
}
window.addEventListener('keydown', handlekey);
shakeButton.addEventListener("click", clearCanvas);