import {ViewModel} from "../utils/ViewModel.js";
import {PreviewViewModel} from "../preview/PreviewViewModel.js";
import {Link} from "../Link.js";

export class CreateLinkViewModel extends ViewModel {
    constructor(options) {
        super(options);
        this._link = null;
        this.previewViewModel = null;
    }

    validateIdentifier(identifier) {
        return Link.validateIdentifier(identifier);
    }

    async createLink(identifier) {
        this._link = Link.parse(identifier);
        if (this._link) {
            this.openLink("#" + this._link.toFragment());
        }
    }
}
