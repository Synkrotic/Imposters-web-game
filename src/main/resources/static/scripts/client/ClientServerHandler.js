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