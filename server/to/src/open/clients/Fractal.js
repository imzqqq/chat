import {Maturity, Platform, LinkKind, FlathubLink} from "../types.js";

/**
 * Information on how to deep link to a given matrix client.
 */
export class Fractal {
    get id() { return "fractal"; }
    get name() { return "Fractal"; }
    get icon() { return "images/client-icons/fractal.png"; }
    get author() { return "Daniel Garcia Moreno"; }
    get homepage() { return "https://gitlab.gnome.org/GNOME/fractal"; }
    get platforms() { return [Platform.Linux]; }
    get description() { return 'Fractal is a Chat Client written in Rust.'; }
    getMaturity(platform) { return Maturity.Beta; }
    getDeepLink(platform, link) {}
    canInterceptMatrixToLinks(platform) { return false; }

    getLinkInstructions(platform, link) {
        if (link.kind === LinkKind.User || link.kind === LinkKind.Room) {
            return "Click the '+' button in the top right and paste the identifier";
        }
    }

    getCopyString(platform, link) {
        if (link.kind === LinkKind.User || link.kind === LinkKind.Room) {
            return link.identifier;
        }
    }

    getInstallLinks(platform) {
        if (platform === Platform.Linux) {
            return [new FlathubLink("org.gnome.Fractal")];
        }
    }

    getPreferredWebInstance(link) {}
}
