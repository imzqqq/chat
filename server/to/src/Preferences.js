import {Platform} from "./Platform.js";
import {EventEmitter} from "./utils/ViewModel.js";

export class Preferences extends EventEmitter {
    constructor(localStorage) {
        super();
        this._localStorage = localStorage;
        this.clientId = null;
        // used to differentiate web from native if a client supports both
        this.platform = null;
        this.homeservers = null;

        const prefsStr = localStorage.getItem("preferred_client");
        if (prefsStr) {
            const {id, platform} = JSON.parse(prefsStr);
            this.clientId = id;
            this.platform = Platform[platform];
        }
        const serversStr = localStorage.getItem("consented_servers");
        if (serversStr) {
            this.homeservers = JSON.parse(serversStr);
        }
    }

    setClient(id, platform) {
        this.clientId = id;
        platform = Platform[platform];
        this.platform = platform;
        this._localStorage.setItem("preferred_client", JSON.stringify({id, platform}));
        this.emit("canClear")
    }

    setHomeservers(homeservers, persist) {
        this.homeservers = homeservers;
        if (persist) {
            this._localStorage.setItem("consented_servers", JSON.stringify(homeservers));
            this.emit("canClear");
        }
    }

    clear() {
        this._localStorage.removeItem("preferred_client");
        this._localStorage.removeItem("consented_servers");
        this.clientId = null;
        this.platform = null;
        this.homeservers = null;
    }

    get canClear() {
        return !!this.clientId || !!this.platform || !!this.homeservers;
    }
}
