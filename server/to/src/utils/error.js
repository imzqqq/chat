export class ConnectionError extends Error {
    constructor(message, isTimeout) {
        super(message || "ConnectionError");
        this.isTimeout = isTimeout;
    }

    get name() {
        return "ConnectionError";
    }
}

export class AbortError extends Error {
    get name() {
        return "AbortError";
    }
}