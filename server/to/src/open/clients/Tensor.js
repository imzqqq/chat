import {Maturity, Platform, LinkKind, FlathubLink,  style} from "../types.js";

export class Tensor {
    get id() { return "tensor"; }
    get name() { return "Tensor"; }
    get icon() { return "images/client-icons/tensor.png"; }
    get author() { return "David A Roberts"; }
    get homepage() { return "https://github.com/davidar/tensor"; }
    get platforms() { return [Platform.Windows, Platform.macOS, Platform.Linux, Platform.Android, Platform.iOS]; }
    get description() { return 'QML and JS cross-platform desktop Chat client'; }
    getMaturity(platform) { return Maturity.Alpha; }
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
        if (platform === Platform.Android) {
            return [new FDroidLink("io.davidar.tensor")];
        }
    }

    getPreferredWebInstance(link) {}
}
