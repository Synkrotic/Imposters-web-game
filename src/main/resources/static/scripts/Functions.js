async function loadPage(filename) {
    const pageLoader = document.getElementById("page-loader");
    const response = await fetch(`/subpages/${filename}.html`);
    pageLoader.innerHTML = await response.text();

    pageLoader.querySelectorAll("script").forEach(oldScript => {
        const newScript = document.createElement("script");
        newScript.textContent = oldScript.textContent;
        oldScript.replaceWith(newScript);
    });
}

async function updatePlayerList(playerNames) {
    const playerList = document.getElementById("player-list");
    playerList.innerHTML = "";

    const response = await fetch(`/components/player-name-item.html`);
    response.text().then(res => {
        let htmlItems = ""
        for (const playerName of playerNames) {
            const elementHtml = res.replaceAll("{{ USER_NAME }}", playerName)
            htmlItems += elementHtml
        }
        playerList.innerHTML = htmlItems
    })
}

function startGame() {
    handler.sendMessage(`${serverPrefix} POST GAME START`)
}

function sendAnswer() {
    const answerEntry = document.getElementById("answer")
    handler.sendMessage(`${serverPrefix} POST GAME ANSWER ${answerEntry.value}`)
}

function sendGuess(guess) {
    handler.sendMessage(`${serverPrefix} POST GAME GUESS ${guess}`)
    loadPage("wait")
}