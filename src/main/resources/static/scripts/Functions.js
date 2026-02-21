async function loadPage(filename) {
    const pageLoader = document.getElementById("page-loader");
    const response = await fetch(`/subpages/${filename}.html`);
    pageLoader.innerHTML = await response.text();
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