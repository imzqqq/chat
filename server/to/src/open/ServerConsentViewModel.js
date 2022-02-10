import {ViewModel} from "../utils/ViewModel.js";
import {ClientListViewModel} from "./ClientListViewModel.js";
import {ClientViewModel} from "./ClientViewModel.js";
import {PreviewViewModel} from "../preview/PreviewViewModel.js";
import {getLabelForLinkKind} from "../Link.js";
import {orderedUnique} from "../utils/unique.js";

export class ServerConsentViewModel extends ViewModel {
    constructor(options) {
        super(options);
        this.servers = options.servers;
        this.done = options.done;
        this.selectedServer = this.servers[0];
        this.showSelectServer = false;
    }

    setShowServers() {
        this.showSelectServer = true;
        this.emitChange();
    }

    selectServer(server) {
        this.selectedServer = server;
        this.emitChange();
    }

    selectOtherServer(domainOrUrl) {
        let urlStr = domainOrUrl;
        if (!urlStr.startsWith("http://") && !urlStr.startsWith("https://")) {
            urlStr = `https://${domainOrUrl}`;
        }
        try {
            const domain = new URL(urlStr).hostname;
            if (/((?:[0-9a-zA-Z][0-9a-zA-Z-]{1,61}\.)+)(xn--[a-z0-9]+|[a-z]+)/.test(domain) || domain === "localhost") {
                this.selectServer(domainOrUrl);
                return true;
            }
        } catch (err) {}
        this.selectServer(null);
        return false;
    }

    continueWithSelection(askEveryTime) {
        // keep previously consented servers
        const homeservers = this.preferences.homeservers || [];
        homeservers.unshift(this.selectedServer);
        this.preferences.setHomeservers(orderedUnique(homeservers), !askEveryTime);
        this.done();
    }

    continueWithoutConsent(askEveryTime) {
        this.preferences.setHomeservers([], !askEveryTime);
        this.done();
    }
}
