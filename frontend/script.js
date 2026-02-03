const heart = document.querySelectorAll(".heart-bg");
const cat = document.getElementById("cat");
const catHint = document.getElementById("hint");

let heartFrame = 1;
let catFrame = 1;

// ❤️ utripajoči srčki
setInterval(() => {
    heartFrame = heartFrame === 1 ? 2 : 1;
    heart.src = `assets/heart${heartFrame}.png`;
}, 500);

// 🐱 mačka se premika
setInterval(() => {
    catFrame = catFrame === 1 ? 2 : 1;
    cat.src = `assets/cat${catFrame}.png`;
}, 700);

const hints = [
    "💬 Spomni se datuma.",
    "💬 Nekaj romantičnega 💖",
    "💬 To ni PIN kartice 😼",
    "💬 Poglej ključavnico..."
];

let hintIndex = 0;

cat.addEventListener("click", () => {
    hintIndex = (hintIndex + 1) % hints.length;
    catHint.textContent = hints[hintIndex];
});

if (data.status === "wrong") {
    heart.style.opacity = "0.3";
}

if (data.status === "correct") {
    catHint.textContent = "💬 Yay! Odklenjeno 💖";
    lock.src = "assets/lock_open.png";
}
