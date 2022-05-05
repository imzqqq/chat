import "matrix-react-sdk/src/@types/global"; // load matrix-react-sdk's type extensions first
import type { Renderer } from "react-dom";

type ElectronChannel =
    "app_onAction" |
    "before-quit" |
    "check_updates" |
    "install_update" |
    "ipcCall" |
    "ipcReply" |
    "loudNotification" |
    "preferences" |
    "seshat" |
    "seshatReply" |
    "setBadgeCount" |
    "update-downloaded" |
    "userDownloadCompleted" |
    "userDownloadOpen";

declare global {
    interface Window {
        mxSendRageshake: (text: string, withLogs?: boolean) => void;
        matrixChat: ReturnType<Renderer>;

        // electron-only
        electron?: Electron;

        // opera-only
        opera?: any;

        // https://developer.mozilla.org/en-US/docs/Web/API/InstallTrigger
        InstallTrigger: any;
    }

    interface Electron {
        on(channel: ElectronChannel, listener: (event: Event, ...args: any[]) => void): void;
        send(channel: ElectronChannel, ...args: any[]): void;
    }

    interface Navigator {
        // PWA badging extensions https://w3c.github.io/badging/
        setAppBadge?(count: number): Promise<void>;
        clearAppBadge?(): Promise<void>;
    }
}

// add method which is missing from the node typing
declare module "url" {
    interface Url {
        format(): string;
    }
}
