import {
    ipcRenderer,
    desktopCapturer,
    contextBridge,
    IpcRendererEvent,
    SourcesOptions,
} from 'electron';

// Expose only expected IPC wrapper APIs to the renderer process to avoid
// handing out generalised messaging access.

const CHANNELS = [
    "app_onAction",
    "before-quit",
    "check_updates",
    "install_update",
    "ipcCall",
    "ipcReply",
    "loudNotification",
    "preferences",
    "seshat",
    "seshatReply",
    "setBadgeCount",
    "update-downloaded",
    "userDownloadCompleted",
    "userDownloadOpen",
];

interface ISource {
    id: string;
    name: string;
    thumbnailURL: string;
}

contextBridge.exposeInMainWorld(
    "electron",
    {
        on(channel: string, listener: (event: IpcRendererEvent, ...args: any[]) => void): void {
            if (!CHANNELS.includes(channel)) {
                console.error(`Unknown IPC channel ${channel} ignored`);
                return;
            }
            ipcRenderer.on(channel, listener);
        },
        send(channel: string, ...args: any[]): void {
            if (!CHANNELS.includes(channel)) {
                console.error(`Unknown IPC channel ${channel} ignored`);
                return;
            }
            ipcRenderer.send(channel, ...args);
        },
        async getDesktopCapturerSources(options: SourcesOptions): Promise<ISource[]> {
            const sources = await desktopCapturer.getSources(options);
            const desktopCapturerSources: ISource[] = [];

            for (const source of sources) {
                desktopCapturerSources.push({
                    id: source.id,
                    name: source.name,
                    thumbnailURL: source.thumbnail.toDataURL(),
                });
            }

            return desktopCapturerSources;
        },
    },
);
