import { Maturity, Platform, LinkKind, FlathubLink, AppleStoreLink, PlayStoreLink, WebsiteLink } from "../types.js";

/**
 * Information on how to deep link to a given matrix client.
 */
export class Fluffychat {
    get id() { return "im.fluffychat"; }
    get name() { return "FluffyChat"; }
    get icon() { return "images/client-icons/fluffychat.svg"; }
    get author() { return "Krille Fear"; }
    get homepage() { return "https://fluffychat.im"; }
    get platforms() {
        return [
            Platform.Android, Platform.iOS,
            Platform.Windows, Platform.macOS, Platform.Linux,
            Platform.DesktopWeb,
        ];
    }
    get description() { return "Chat with your friends using the cutest messenger in the Chat network"; }
    getMaturity(platform) {
        switch (platform) {
            case Platform.Android: return Maturity.Stable;
            case Platform.iOS: return Maturity.Stable;
            case Platform.DesktopWeb: return Maturity.Stable;
            case Platform.Linux: return Maturity.Stable;
            case Platform.macOS: return Maturity.Beta;
            case Platform.Windows: return Maturity.Beta;
        }
    }

    getInstallLinks(platform) {
        switch (platform) {
            case Platform.iOS: return [new AppleStoreLink("fluffychat", "id1551469600")];
            case Platform.Android: return [new PlayStoreLink("chat.fluffy.fluffychat")];
            case Platform.Linux: return [new FlathubLink("im.fluffychat.Fluffychat")];
            default: return [new WebsiteLink("https://fluffychat.im")];
        }
    }

    getLinkInstructions(platform, link) {
        if (link.kind === LinkKind.User) {
            switch (platform) {
                case Platform.Android: return;
                case Platform.DesktopWeb: return "Open the web app at https://fluffychat.im/web/ and log in to your account. Click on '+' and paste the username.";
                default: return "Open the app and click on '+' and paste the username.";
            }
        }
        if (link.kind === LinkKind.Room) {
            switch (platform) {
                case Platform.Android: return;
                case Platform.DesktopWeb: return "Open the web app at https://fluffychat.im/web/ and log in to your account. Click on 'Discover' and paste the identifier.";
                default: return "Open the app on your device. Click on 'Discover' and paste the identifier.";
            }
        }
    }

    getCopyString(platform, link) {
        if (link.kind === LinkKind.User || link.kind === LinkKind.Room) {
            return link.identifier;
        }
    }

    getDeepLink(platform, link) { }
    canInterceptMatrixToLinks(platform) {
        return platform === Platform.Android;
    }

    getPreferredWebInstance(link) {}
}
