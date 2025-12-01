document.addEventListener("DOMContentLoaded", function () {
    /*Drop Menu*/
    const toggleBtn = document.getElementById("menuToggle");
    const dropdown = document.getElementById("dropdownContent");
    const hofBtn = document.getElementById("hofBtn");
    const modal = document.getElementById("hofModal");
    const closeModal = document.getElementById("closeModal");

    if (toggleBtn && dropdown) {
        toggleBtn.addEventListener("click", e => {
            e.stopPropagation();
            dropdown.classList.toggle("show");
        });
        document.addEventListener("click", () => dropdown.classList.remove("show"));
    }
    if (hofBtn && modal && closeModal) {
        hofBtn.addEventListener("click", e => {
            e.preventDefault();
            dropdown?.classList.remove("show");
            modal.style.display = "block";
        });
        closeModal.addEventListener("click", () => modal.style.display = "none");
    }

    const grid = document.querySelector(".grid");

    /*Swap*/
    const swapBtn = document.getElementById("swap-btn");
    let swapMode = false;
    let firstCell = null;
    swapBtn?.addEventListener("click", () => {
        swapMode = !swapMode;
        firstCell = null;
        grid.classList.toggle("swap-active", swapMode);
        swapBtn.classList.toggle("active", swapMode);
        if (!swapMode) {
            grid.querySelectorAll(".tile.selected")
                .forEach(t => t.classList.remove("selected"));
        }
    });

    document.querySelectorAll(".tile").forEach(tile => {
        tile.addEventListener("click", () => {
            if (!swapMode) return;
            const value = tile.textContent.trim();
            const r = tile.dataset.row, c = tile.dataset.col;

            if (firstCell === null) {
                if (!value) return;
                firstCell = {r, c};
                tile.classList.add("selected");
            } else {
                swapMode = false;
                grid.classList.remove("swap-active");
                swapBtn.classList.remove("active");
                grid.querySelectorAll(".tile.selected")
                    .forEach(t => t.classList.remove("selected"));
                window.location.href = `/1024/swap?r1=${firstCell.r}&c1=${firstCell.c}&r2=${r}&c2=${c}`;
            }
        });
    });

    /*Delete*/
    const deleteBtn = document.getElementById("delete-btn");
    let deleteMode = false;
    deleteBtn?.addEventListener("click", e => {
        e.stopPropagation();
        deleteMode = !deleteMode;
        grid.classList.toggle("delete-active", deleteMode);
        deleteBtn.classList.toggle("active", deleteMode);
        if (!deleteMode) {
            grid.querySelectorAll(".tile.delete-empty")
                .forEach(t => t.classList.remove("delete-empty"));
        }
    });

    document.querySelectorAll(".tile").forEach(tile => {
        tile.addEventListener("mouseover", () => {
            if (!deleteMode) return;
            const v = tile.textContent.trim();
            if (!v) return;  // пустые клетки игнорируем
            document.querySelectorAll(".tile").forEach(t => {
                if (t.textContent.trim() === v) {
                    t.classList.add("delete-empty");
                }
            });
        });
        tile.addEventListener("mouseout", () => {
            document.querySelectorAll(".tile.delete-empty")
                .forEach(t => t.classList.remove("delete-empty"));
        });
        tile.addEventListener("click", () => {
            if (!deleteMode) return;
            const value = tile.textContent.trim();
            if (value) {
                deleteMode = false;
                grid.classList.remove("delete-active");
                deleteBtn.classList.remove("active");
                window.location.href = `/1024/delete?value=${value}`;
            }
        });
    });
});

/*Email verifycation*/
async function emailVerificationCodeQuery() {
    event.preventDefault();
    const email = document.getElementById("email").value.trim();
    const login = document.getElementById("login").value.trim();

    const loginUsed = await $.get('/api/user/1024/isLoginUsed/' + login);
    const emailUsed = await $.get('/api/user/1024/isEmailUsed/' + email);

    if (!loginUsed && !emailUsed) {
        const sentCode = await $.get('/api/send-email/verification/' + email);
        let tries = 3, code;
        do {
            code = prompt(`Enter registration code sent to ${email}. You have ${tries} tries:`);
            if (code === null) return;
            tries--;
        } while (code !== sentCode && tries > 0);

        if (code === sentCode) {
            document.querySelector('form.auth-form').submit();
        } else {
            alert("Incorrect verification code for registration.");
        }
    } else {
        alert(emailUsed
            ? "An account with this email already exists."
            : "An account with this login already exists.");
    }
}

/* Password‐reset verification */
async function resetPassword() {
    event.preventDefault();
    const login = document.getElementById("login").value.trim();
    const exists = await $.get('/api/user/1024/isLoginUsed/' + login);

    if (!exists) {
        alert("No account found with this login.");
        return;
    }

    const email = await $.get('/api/user/1024/getUserEmail/' + login);
    const sentCode = await $.get('/api/send-email/reset/' + email);
    let tries = 3, code;
    do {
        code = prompt(`Enter password‐reset code sent to ${email}. You have ${tries} tries:`);
        if (code === null) return;
        tries--;
    } while (code !== sentCode && tries > 0);

    if (code === sentCode) {
        document.querySelector('form.auth-form').submit();
    } else {
        alert("Incorrect password‐reset code.");
    }
}

/*Win/Lose*/
document.addEventListener("DOMContentLoaded", function () {
    const overlayContainer = document.getElementById("gameOverlay");
    const status = document.body.dataset.status;
    const mode = document.body.dataset.mode || "";
    const newPath = mode === "easy" ? "/1024/newEasy" : mode === "hard" ? "/1024/newHard" : "/1024/newNormal";

    if (status === "won") {
        const winOverlay = document.createElement("div");
        winOverlay.className = "overlay-message";
        winOverlay.innerHTML = `
            <div class="overlay-win" style="display:flex;flex-direction:column;align-items:center;">
                Congratulations! You won!<br><br>
                <a href="${newPath}" class="btn">Play Again</a><br>
                <a href="/1024/reviews" class="btn">Leave Feedback</a>
            </div>`;
        overlayContainer.appendChild(winOverlay);

        const end = Date.now() + 3000;
        (function frame() {
            confetti({particleCount: 5, angle: 60, spread: 55, origin: {x: 0}});
            confetti({particleCount: 5, angle: 120, spread: 55, origin: {x: 1}});
            if (Date.now() < end) requestAnimationFrame(frame);
        })();
    }

    if (status === "lost") {
        const loseOverlay = document.createElement("div");
        loseOverlay.className = "overlay-message overlay-lose shake";
        loseOverlay.innerHTML = `
            <div style="display:flex;flex-direction:column;align-items:center;">
                Game Over!<br><br>
                <a href="${newPath}" class="btn">Try Again</a><br>
                <a href="/1024/reviews" class="btn">Leave Feedback</a>
            </div>`;
        overlayContainer.appendChild(loseOverlay);

        document.querySelector(".grid")?.classList.add("shake");
    }

    /*Keyboard input*/
    document.addEventListener("keydown", e => {
        let btn = null;
        switch (e.key) {
            case "ArrowUp": btn = document.querySelector(".arrow-up");break;
            case "ArrowDown": btn = document.querySelector(".arrow-down");break;
            case "ArrowLeft": btn = document.querySelector(".arrow-left");break;
            case "ArrowRight": btn = document.querySelector(".arrow-right");break;
            default:return;
        }
        e.preventDefault();
        btn.classList.add("hovered");
        setTimeout(() => {
            btn.classList.remove("hovered");
            btn.click();
        }, 150);
    });
});
