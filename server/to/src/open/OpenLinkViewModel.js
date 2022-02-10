import {ViewModel} from "../utils/ViewModel.js";
import {ClientListViewModel} from "./ClientListViewModel.js";
import {ClientViewModel} from "./ClientViewModel.js";
import {PreviewViewModel} from "../preview/PreviewViewModel.js";
import {ServerConsentViewModel} from "./ServerConsentViewModel.js";
import {getLabelForLinkKind} from "../Link.js";
import {orderedUnique} from "../utils/unique.js";

export class OpenLinkViewModel extends ViewModel {
    constructor(options) {
        super(options);
        const {clients, link} = options;
        this._link = link;
        this._clients = clients;
        this.serverConsentViewModel = null;
        this.previewViewModel = null;
        this.clientsViewModel = null;
        this.previewLoading = false;
        if (this.preferences.homeservers === null) {
            this._showServerConsent();
        } else {
            this._showLink();
        }
    }

    _showServerConsent() {
        let servers = [];
        if (this.preferences.homeservers) {
            servers.push(...this.preferences.homeservers);
        }
        servers.push(...this._link.servers);
        servers = orderedUnique(servers);
        this.serverConsentViewModel = new ServerConsentViewModel(this.childOptions({
            servers,
            done: () => {
                this.serverConsentViewModel = null;
                this._showLink();
            }
        }));
    }

    async _showLink() {
        const clientId = this.preferences.clientId || this._link.clientId;
        const preferredClient = clientId ? this._clients.find(c => c.id === clientId) : null;
        this.clientsViewModel = new ClientListViewModel(this.childOptions({
            clients: this._clients,
            link: this._link,
            client: preferredClient,
        }));
        this.previewViewModel = new PreviewViewModel(this.childOptions({
            link: this._link,
            consentedServers: this.preferences.homeservers
        }));
        this.previewLoading = true;
        this.emitChange();
        await this.previewViewModel.load();
        this.previewLoading = false;
        this.emitChange();
    }

    get previewDomain() {
        return this.previewViewModel?.domain;
    }

    get previewFailed() {
        return this.previewViewModel?.failed;
    }

    get showClientsLabel() {
        return getLabelForLinkKind(this._link.kind);
    }

    changeServer() {
        this.previewViewModel = null;
        this.clientsViewModel = null;
        this._showServerConsent();
        this.emitChange();
    }
}
