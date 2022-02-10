import {createEnum} from "../utils/enum.js";
export const Maturity = createEnum("Alpha", "Beta", "Stable");
export {LinkKind} from "../Link.js";
export {Platform} from "../Platform.js";

export class AppleStoreLink {
    constructor(org, appId) {
        this._org = org;
        this._appId = appId;
    }

    createInstallURL(link) {
        return `https://apps.apple.com/app/${encodeURIComponent(this._org)}/${encodeURIComponent(this._appId)}`;
    }

    get channelId() {
        return "apple-app-store";
    }

    getDescription() {
        return "Download on the App Store";
    }
}

export class PlayStoreLink {
    constructor(appId) {
        this._appId = appId;
    }

    createInstallURL(link) {
        return `https://play.google.com/store/apps/details?id=${encodeURIComponent(this._appId)}&referrer=${encodeURIComponent(link.identifier)}`;
    }
      
    get channelId() {
        return "play-store";
    }

    getDescription() {
        return "Get it on Google Play";
    }
}

export class FDroidLink {
    constructor(appId) {
        this._appId = appId;
    }

    createInstallURL(link) {
        return `https://f-droid.org/packages/${encodeURIComponent(this._appId)}`;
    }

    get channelId() {
        return "fdroid";
    }

    getDescription() {
        return "Get it on F-Droid";
    }
}

export class FlathubLink {
    constructor(appId) {
        this._appId = appId;
    }

    createInstallURL(link) {
        return `https://flathub.org/apps/details/${encodeURIComponent(this._appId)}`;
    }

    get channelId() {
        return "flathub";
    }

    getDescription() {
        return "Get it on Flathub";
    }
}

export class WebsiteLink {
    constructor(url) {
        this._url = url;
    }

    createInstallURL(link) {
        return this._url;
    }

    get channelId() {
        return "website";
    }

    getDescription(platform) {
        return `Download for ${platform}`;
    }
}

export const style = {
    code(text) {
        return {type: "code", text};
    }
}
