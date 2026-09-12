const cat = document.getElementById("cat");
const button = document.querySelector("#button");
const input = document.getElementById("password");
const hintText = document.getElementById("hint-text");
const container = document.getElementById("container");


/* ----------------------------
   HINT TEXT + KATEX
---------------------------- */

function fitText(element) {

    let fontSize = 50;

    const minFontSize = 8;

    element.style.fontSize = fontSize + "px";

    while (
        (
            element.scrollWidth > element.clientWidth ||
            element.scrollHeight > element.clientHeight
        ) &&
        fontSize > minFontSize
    ) {
        fontSize--;

        element.style.fontSize = fontSize + "px";
    }
}


function updateText(newText) {

    hintText.innerHTML = "";

    // KaTeX
    if (newText.includes("\\") && window.katex) {

        hintText.style.fontSize = "";

        katex.render(newText, hintText, {
            throwOnError: false
        });

    } else {

        hintText.textContent = newText;

        fitText(hintText);

    }
}


/* ----------------------------
   UI SCALE
---------------------------- */

function scaleUI() {

    const baseWidth = 900;
    const baseHeight = 650;

    const scaleX = window.innerWidth / baseWidth;
    const scaleY = window.innerHeight / baseHeight;

    const scale = Math.min(scaleX, scaleY);

    container.style.transform =
        `translate(-50%, -50%) scale(${scale})`;
}

window.addEventListener("resize", scaleUI);

scaleUI();


/* ----------------------------
   CAT ANIMATION
---------------------------- */

let catToggle = false;

setInterval(() => {

    cat.src = catToggle
        ? "cat1.png"
        : "cat2.png";

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

    updateText(`Zaklenjeno... ${timeLeft}s`);

    countdownInterval = setInterval(() => {

        timeLeft--;

        if (timeLeft <= 0) {

            clearInterval(countdownInterval);

            countdownInterval = null;

            updateText("Poskusi spet");

            setLocked(false);

            input.focus();

            return;
        }

        updateText(`Zaklenjeno... ${timeLeft}s`);

    }, 1000);
}


/* ----------------------------
   API CALL
---------------------------- */

function checkPassword() {

    if (locked) return;

    fetch("http://localhost:4567/check", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            password: input.value
        })

    })

    .then(res => res.json())

    .then(data => {

        // Prikaži hint ali KaTeX
        updateText(data.message);

        // Reset input
        input.value = "";

        // Pravilno
        if (data.status === "correct") {

            setLocked(true);

            return;
        }

        // Napačno + zmanjka poskusov
        if (
            data.status === "wrong" &&
            data.attemptsLeft === 0
        ) {

            startCountdown(5);

        } else {

            input.focus();

        }

    })

    .catch(() => {

        updateText("Muca ne najde strežnika");

    });
}


/* ----------------------------
   BUTTON
---------------------------- */

button.addEventListener("click", checkPassword);


/* ----------------------------
   ENTER KEY
---------------------------- */

input.addEventListener("keydown", (e) => {

    if (e.key === "Enter") {

        checkPassword();

    }

});