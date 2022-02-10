import {Maturity, Platform, LinkKind, WebsiteLink, style} from "../types.js";

/**
 * Information on how to deep link to a given matrix client.
 */
export class Weechat {
    get id() { return "weechat"; }
    get name() { return "Weechat"; }
    get icon() { return "images/client-icons/weechat.svg"; }
    get author() { return "Poljar"; }
    get homepage() { return "https://github.com/poljar/weechat-matrix"; }
    get platforms() { return [Platform.Windows, Platform.macOS, Platform.Linux]; }
    get description() { return 'Command-line Chat interface using Weechat.'; }
    getMaturity(platform) { return Maturity.Beta; }
    getDeepLink(platform, link) {}
    canInterceptMatrixToLinks(platform) { return false; }

    getLinkInstructions(platform, link) {
        switch (link.kind) {
            case LinkKind.User: return [`Type `, style.code(`/invite ${link.identifier}`)];
            case LinkKind.Room: return [`Type `, style.code(`/join ${link.identifier}`)];
        }
    }

    getCopyString(platform, link) {
        switch (link.kind) {
            case LinkKind.User: return `/invite ${link.identifier}`;
            case LinkKind.Room: return `/join ${link.identifier}`;
        }
    }

    getInstallLinks(platform) {}

    getPreferredWebInstance(link) {}
}
