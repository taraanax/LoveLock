const cat = document.getElementById("cat");
const button = document.getElementById("button");
const input = document.getElementById("password");
const hintText = document.getElementById("hint-text");

let catToggle = false;

// animacija mačke
setInterval(() => {
cat.src = catToggle ? "cat1.png" : "cat2.png";
catToggle = !catToggle;
}, 500);

button.addEventListener("click", () => {
fetch("http://localhost:4567/check", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({ password: input.value })
})
.then(res => res.json())
.then(data => {
hintText.innerText = data.message;

if (data.status === "correct") {
hintText.innerText = "ODKLENJENO 💙";
}
})
.catch(() => {
hintText.innerText = "Muca ne najde strežnika 😿";
});
});

