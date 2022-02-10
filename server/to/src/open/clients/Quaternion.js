import {Maturity, Platform, LinkKind, FlathubLink, style} from "../types.js";

export class Quaternion {
    get id() { return "quaternion"; }
    get name() { return "Quaternion"; }
    get icon() { return "images/client-icons/quaternion.svg"; }
    get author() { return "Felix Rohrbach"; }
    get homepage() { return "https://github.com/Fxrh/Quaternion"; }
    get platforms() { return [Platform.Windows, Platform.macOS, Platform.Linux]; }
    get description() { return 'Qt5 and C++ cross-platform desktop Chat client.'; }
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

    getInstallLinks(platform) {
        if (platform === Platform.Linux) {
            return [new FlathubLink("com.github.quaternion")];
        }
    }

    getPreferredWebInstance(link) {}
}
