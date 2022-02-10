import { app, Tray, Menu, nativeImage } from "electron";
import pngToIco from "png-to-ico";
import path from "path";
import fs from "fs";
import { _t } from "./language-helper";

let trayIcon: Tray = null;

export function hasTray(): boolean {
    return (trayIcon !== null);
}

export function destroy(): void {
    if (trayIcon) {
        trayIcon.destroy();
        trayIcon = null;
    }
}

function toggleWin(): void {
    if (global.mainWindow.isVisible() && !global.mainWindow.isMinimized()) {
        global.mainWindow.hide();
    } else {
        if (global.mainWindow.isMinimized()) global.mainWindow.restore();
        if (!global.mainWindow.isVisible()) global.mainWindow.show();
        global.mainWindow.focus();
    }
}

const showWin = function() {
    if (global.mainWindow.isMinimized()) global.mainWindow.restore();
    if (!global.mainWindow.isVisible()) global.mainWindow.show();
    global.mainWindow.focus();
};

const hideWin = function() {
    global.mainWindow.hide();
};

interface IConfig {
    icon_path: string; // eslint-disable-line camelcase
    brand: string;
}

export function create(config: IConfig): void {
    // no trays on darwin
    if (process.platform === 'darwin' || trayIcon) return;
    const defaultIcon = nativeImage.createFromPath(config.icon_path);

    trayIcon = new Tray(defaultIcon);
    trayIcon.setToolTip(config.brand);
    initApplicationMenu();
    trayIcon.on('click', toggleWin);

    let lastFavicon = null;
    global.mainWindow.webContents.on('page-favicon-updated', async function(ev, favicons) {
        if (!favicons || favicons.length <= 0 || !favicons[0].startsWith('data:')) {
            if (lastFavicon !== null) {
                global.mainWindow.setIcon(defaultIcon);
                trayIcon.setImage(defaultIcon);
                lastFavicon = null;
            }
            return;
        }

        // No need to change, shortcut
        if (favicons[0] === lastFavicon) return;
        lastFavicon = favicons[0];

        let newFavicon = nativeImage.createFromDataURL(favicons[0]);

        // Windows likes ico's too much.
        if (process.platform === 'win32') {
            try {
                const icoPath = path.join(app.getPath('temp'), 'win32_element_icon.ico');
                fs.writeFileSync(icoPath, await pngToIco(newFavicon.toPNG()));
                newFavicon = nativeImage.createFromPath(icoPath);
            } catch (e) {
                console.error("Failed to make win32 ico", e);
            }
        }

        trayIcon.setImage(newFavicon);
        global.mainWindow.setIcon(newFavicon);
    });

    global.mainWindow.webContents.on('page-title-updated', function(ev, title) {
        trayIcon.setToolTip(title);
    });
}

export function initApplicationMenu(): void {
    if (!trayIcon) {
        return;
    }

    const contextMenu = Menu.buildFromTemplate([
        // {
        //     label: _t('Show/Hide'),
        //     click: toggleWin,
        // },
        {
            label: _t('Show'),
            click: showWin,
        },
        {
            label: _t('Hide'),
            click: hideWin,
        },
        { type: 'separator' },
        {
            label: _t('Quit'),
            click: function() {
                app.quit();
            },
        },
    ]);

    trayIcon.setContextMenu(contextMenu);
}
