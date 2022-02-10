import {isWebPlatform, Platform} from "../Platform.js";
import {Maturity} from "./types.js";
import {ClientViewModel} from "./ClientViewModel.js";
import {ViewModel} from "../utils/ViewModel.js";

export class ClientListViewModel extends ViewModel {
    constructor(options) {
        super(options);
        const {clients, client, link} = options;
        this._clients = clients;
        this._link = link;
        this.clientList = null;
        this._showExperimental = false;
        this._showUnsupportedPlatforms = false;
        this._filterClients();
        this.clientViewModel = null;
        if (client) {
            this._pickClient(client);
        }
    }

    get showUnsupportedPlatforms() {
        return this._showUnsupportedPlatforms;
    }

    get showExperimental() {
        return this._showExperimental;
    }

    set showUnsupportedPlatforms(enabled) {
        this._showUnsupportedPlatforms = enabled;
        this._filterClients();
    }

    set showExperimental(enabled) {
        this._showExperimental = enabled;
        this._filterClients();
    }

    _filterClients() {
        const clientVMs = this._clients.filter(client => {
            const platformMaturities = this.platforms.map(p => client.getMaturity(p));
            const isStable = platformMaturities.includes(Maturity.Stable) || platformMaturities.includes(Maturity.Beta);
            const isSupported = client.platforms.some(p => this.platforms.includes(p));
            if (!this._showExperimental && !isStable) {
                return false;
            }
            if (!this._showUnsupportedPlatforms && !isSupported) {
                return false;
            }
            return true;
        }).map(client => new ClientViewModel(this.childOptions({
            client,
            link: this._link,
            pickClient: client => this._pickClient(client)
        })));
        const preferredClientVMs = clientVMs.filter(c => c.hasPreferredWebInstance);
        const otherClientVMs = clientVMs.filter(c => !c.hasPreferredWebInstance);
        this.clientList = preferredClientVMs.concat(otherClientVMs);
        this.emitChange();
    }

    _pickClient(client) {
        this.clientViewModel = this.clientList.find(vm => vm.clientId === client.id);
        this.clientViewModel.pick(this);
        this.emitChange();
    }

    showAll() {
        this.clientViewModel = null;
        this.emitChange();
    }
}
