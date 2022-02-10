export class EventEmitter {
    constructor() {
        this._handlersByName = {};
    }

    emit(name, ...values) {
        const handlers = this._handlersByName[name];
        if (handlers) {
            for(const h of handlers) {
                h(...values);
            }
        }
    }

    on(name, callback) {
        let handlers = this._handlersByName[name];
        if (!handlers) {
            this._handlersByName[name] = handlers = new Set();
        }
        handlers.add(callback);
        return () => {
            this.off(name, callback);
        }
    }

    off(name, callback) {
        const handlers = this._handlersByName[name];
        if (handlers) {
            handlers.delete(callback);
            if (handlers.length === 0) {
                delete this._handlersByName[name];
            }
        }
    }
}

export class ViewModel extends EventEmitter {
    constructor(options) {
        super();
        this._options = options;
    }

    emitChange() {
        this.emit("change");
    }

    get request() { return this._options.request; }
    get origin() { return this._options.origin; }
    get openLink() { return this._options.openLink; }
    get platforms() { return this._options.platforms; }
    get preferences() { return this._options.preferences; }

    childOptions(options = {}) {
        return Object.assign({
            request: this.request,
            origin: this.origin,
            openLink: this.openLink,
            platforms: this.platforms,
            preferences: this.preferences,
        }, options);
    }
}
