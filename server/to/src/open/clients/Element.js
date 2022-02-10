import {Maturity, Platform, LinkKind,
    FDroidLink, AppleStoreLink, PlayStoreLink, WebsiteLink} from "../types.js";

const trustedWebInstances = [
    "web.chat.dingshunyu.top",   // first one is the default one
    "develop.element.io",
    "chat.fosdem.org",
    "chat.mozilla.org",
    "webchat.kde.org",
];

/**
 * Information on how to deep link to a given matrix client.
 */
export class Element {
    get id() { return "element.io"; }

    get platforms() {
        return [
            Platform.Android, Platform.iOS,
            Platform.Windows, Platform.macOS, Platform.Linux,
            Platform.DesktopWeb
        ];
    }

    get icon() { return "images/client-icons/chat.svg"; }
    get appleAssociatedAppId() { return "7J4U792NQT.im.vector.app"; }
    get name() {return "Chat"; }
    get description() { return 'Fully-featured Chat client, used by millions.'; }
    get homepage() { return "https://apps.chat.dingshunyu.top"; }
    get author() { return "imqzzZ"; }
    getMaturity(platform) { return Maturity.Stable; }

    getDeepLink(platform, link) {
        let fragmentPath;
        switch (link.kind) {
            case LinkKind.User:
                fragmentPath = `user/${link.identifier}`;
                break;
            case LinkKind.Room:
                fragmentPath = `room/${link.identifier}`;
                break;
            case LinkKind.Group:
                fragmentPath = `group/${link.identifier}`;
                break;
            case LinkKind.Event:
                fragmentPath = `room/${link.identifier}/${link.eventId}`;
                break;
        }
        const isWebPlatform = platform === Platform.DesktopWeb || platform === Platform.MobileWeb;
        if (isWebPlatform || platform === Platform.iOS) {
            let instanceHost = trustedWebInstances[0];
            // we use app.element.io which iOS will intercept, but it likely won't intercept any other trusted instances
            // so only use a preferred web instance for true web links.
            if (isWebPlatform && trustedWebInstances.includes(link.webInstances[this.id])) {
                instanceHost = link.webInstances[this.id];
            }
            return `https://${instanceHost}/#/${fragmentPath}`;
        } else if (platform === Platform.Linux || platform === Platform.Windows || platform === Platform.macOS) {
            return `element://vector/webapp/#/${fragmentPath}`;
        } else {
            return `element://${fragmentPath}`;
        }
    }

    getLinkInstructions(platform, link) {}
    getCopyString(platform, link) {}
    getInstallLinks(platform) {
        switch (platform) {
            case Platform.iOS: return [new AppleStoreLink('vector', 'id1083446067')];
            case Platform.Android: return [new PlayStoreLink('im.vector.app'), new FDroidLink('im.vector.app')];
            default: return [new WebsiteLink("https://chat.dingshunyu.top/get-started")];
        }
    }

    canInterceptMatrixToLinks(platform) {
        return platform === Platform.Android;
    }

    getPreferredWebInstance(link) {
        const idx = trustedWebInstances.indexOf(link.webInstances[this.id])
        return idx === -1 ? undefined : trustedWebInstances[idx];
    }
}
