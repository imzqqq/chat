import {ViewModel} from "../utils/ViewModel.js";
import {resolveServer} from "../preview/HomeServer.js";

export class LoadServerPolicyViewModel extends ViewModel {
    constructor(options) {
        super(options);
        this.server = options.server;
        this.message = `Looking up ${this.server} privacy policy…`;
        this.loading = false;
    }

    async load() {
        this.loading = true;
        this.emitChange();
        try {
            const homeserver = await resolveServer(this.request, this.server);
            if (homeserver) {
                const url = await homeserver.getPrivacyPolicyUrl();
                if (url) {
                    this.message = `Loading ${this.server} privacy policy now…`;
                    this.openLink(url);
                } else {
                    this.loading = false;
                    this.message = `${this.server} does not declare a privacy policy.`;
                }
            } else {
                this.loading = false;
                this.message = `${this.server} does not look like a matrix homeserver.`;
            }
        } catch (err) {
            this.loading = false;
            this.message = `Failed to get the privacy policy for ${this.server}`;
        }
        this.emitChange();
    }
}
