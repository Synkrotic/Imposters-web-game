class HostServerHandler {
    server;
    statusElement;

    constructor(statusElement) {
        this.statusElement = statusElement;
        this.setStatus(ConnectionStatus.not_connected);
    }

    setStatus(status) {
        this.statusElement.innerText = status;
        this.onStatusChanged(status);
    }

    login(name, pwd) {
        let url = `${serverUrl}?name=${name}&pwd=${pwd}`;
        this.server = new WebSocket(url);

        this.server.onopen = this.onOpen.bind(this);
        this.server.onclose = this.onClose.bind(this);
        this.server.onmessage = this.onMessage.bind(this);
        this.server.onerror = this.onError.bind(this);
    }

    onOpen() {
        console.log(serverPrefix, "You are connected to the game server!");
        this.setStatus(ConnectionStatus.conn_admin);

    }
    onClose(event) {
        console.error(serverPrefix, "You have disconnected from the game server!");
        console.error(serverPrefix, "Reason:", event.reason);
        this.setStatus(ConnectionStatus.disconnected);
    }

    onMessage(event){
        console.log(serverPrefix, "Received:", event.data);
        if (event.data.toString().startsWith("[SERVER] PLAYER ")) {
            try {
                this.sendAndReceive(`${serverPrefix} GET PLAYER-LIST`).then(res => {
                    console.log(res);
                    let playersList = JSON.parse(res.toString());
                    updatePlayerList(playersList)
                    console.log("updated list");
                });
            } catch (error) { }
        }
    }

    onError(error) {
        console.error(serverPrefix, "Error occured:", error);
        this.setStatus(ConnectionStatus.failed);
    }

    sendMessage(message) {
        if (this.server.readyState === WebSocket.OPEN) {
            this.server.send(message);
        }
    }

    onStatusChanged(status) {
        switch (status) {
            case ConnectionStatus.not_connected: {
                loadPage("admin-login");
                break;
            }
            case ConnectionStatus.conn_admin: {
                loadPage("admin-dash");
                break;
            }
        }
    }

    sendAndReceive(message) {
        const TIMEOUT_DURATION = 3000;

        return new Promise((resolve, reject) => {
            this.sendMessage(message)

            const timer = setTimeout(() => {
                this.resetMessageSystem()
                reject(new Error('Request timed out'));
            }, TIMEOUT_DURATION);

            this.server.onmessage = (event) => {
                clearTimeout(timer);
                this.resetMessageSystem()
                resolve(event.data);
            };

            this.server.onerror = (error) => {
                clearTimeout(timer);
                this.resetMessageSystem()
                reject(error);
            };
        });
    }

    resetMessageSystem() {
        this.server.onmessage = this.onMessage.bind(this);
        this.server.onerror = this.onError.bind(this);
    }
}