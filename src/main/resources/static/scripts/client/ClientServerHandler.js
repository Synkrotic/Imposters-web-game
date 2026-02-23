class ClientServerHandler {
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

    login(loginName) {
        let url = `${serverUrl}?name=${loginName}`;
        this.server = new WebSocket(url);

        this.server.onopen = this.onOpen.bind(this);
        this.server.onclose = this.onClose.bind(this);
        this.server.onmessage = this.onMessage.bind(this);
        this.server.onerror = this.onError.bind(this);
    }

    onOpen() {
        console.log(serverPrefix, "You are connected to the game server!");
        this.setStatus(ConnectionStatus.connected);

    }
    onClose(event) {
        console.error(serverPrefix, "You have disconnected from the game server!");
        console.error(serverPrefix, "Reason:", event.reason);
        this.setStatus(ConnectionStatus.disconnected);
    }
    
    onMessage(event) {
        console.log(serverPrefix, "Received:", event.data);
        if (event.data.toString().startsWith(loadScreenMessage)) {
            let screen = event.data.toString().replaceAll(loadScreenMessage, "")
            loadPage(screen)
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

    onStatusChanged(status) {
        switch (status) {
            case ConnectionStatus.not_connected: {
                loadPage("login");
                break;
            }
            case ConnectionStatus.connected: {
                loadPage("empty")
                break;
            }
        }
    }
}