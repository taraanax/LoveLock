const cat = document.getElementById("cat");
const button = document.getElementById("button");
const input = document.getElementById("password");
const hintText = document.getElementById("hint-text");
const container = document.getElementById("container");

/* ----------------------------
UI SCALE (fixed ratio)
---------------------------- */
function scaleUI() {
const baseWidth = 900;
const baseHeight = 650;

const scaleX = window.innerWidth / baseWidth;
const scaleY = window.innerHeight / baseHeight;

const scale = Math.min(scaleX, scaleY);

container.style.transform = `translate(-50%, -50%) scale(${scale})`;
}

window.addEventListener("resize", scaleUI);
scaleUI();

/* ----------------------------
CAT ANIMATION
---------------------------- */
let catToggle = false;
setInterval(() => {
cat.src = catToggle ? "cat1.png" : "cat2.png";
catToggle = !catToggle;
}, 500);

/* ----------------------------
LOCKOUT COUNTDOWN
---------------------------- */
let locked = false;
let countdownInterval = null;

function setLocked(state) {
locked = state;

if (state) {
button.style.opacity = "0.5";
button.style.pointerEvents = "none";
input.disabled = true;
input.style.opacity = "0.7";
} else {
button.style.opacity = "1";
button.style.pointerEvents = "auto";
input.disabled = false;
input.style.opacity = "1";
}
}

function startCountdown(seconds) {
let timeLeft = seconds;

setLocked(true);
hintText.innerText = `Zaklenjeno... ${timeLeft}s`;

countdownInterval = setInterval(() => {
timeLeft--;

if (timeLeft <= 0) {
clearInterval(countdownInterval);
countdownInterval = null;

// po unlocku
hintText.innerText = "Poskusi spet 😼";
setLocked(false);
return;
}

hintText.innerText = `Zaklenjeno... ${timeLeft}s`;
}, 1000);
}

/* ----------------------------
API CALL
---------------------------- */
function checkPassword() {
if (locked) return;

fetch("http://localhost:4567/check", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({ password: input.value })
})
.then(res => res.json())
.then(data => {

// MUCA GOVORI BACKEND MESSAGE
hintText.innerText = data.message;

// pravilno
if (data.status === "correct") {
setLocked(true); // da je "done"
return;
}

// napačno, attempts 0 => lockout
if (data.status === "wrong" && data.attemptsLeft === 0) {
startCountdown(5);
}
})
.catch(() => {
hintText.innerText = "Muca ne najde strežnika 😿";
});
}

button.addEventListener("click", checkPassword);

input.addEventListener("keydown", (e) => {
if (e.key === "Enter") checkPassword();
});

