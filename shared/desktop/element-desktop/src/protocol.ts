import { app } from "electron";
import { URL } from "url";
import path from "path";
import fs from "fs";

const PROTOCOL = "element://";
const SEARCH_PARAM = "element-desktop-ssoid";
const STORE_FILE_NAME = "sso-sessions.json";

// we getPath userData before electron-main changes it, so this is the default value
const storePath = path.join(app.getPath("userData"), STORE_FILE_NAME);

function processUrl(url: string): void {
    if (!global.mainWindow) return;
    console.log("Handling link: ", url);
    global.mainWindow.loadURL(url.replace(PROTOCOL, "vector://"));
}

function readStore(): object {
    try {
        const s = fs.readFileSync(storePath, { encoding: "utf8" });
        const o = JSON.parse(s);
        return typeof o === "object" ? o : {};
    } catch (e) {
        return {};
    }
}

function writeStore(data: object): void {
    fs.writeFileSync(storePath, JSON.stringify(data));
}

export function recordSSOSession(sessionID: string): void {
    const userDataPath = app.getPath('userData');
    const store = readStore();
    for (const key in store) {
        // ensure each instance only has one (the latest) session ID to prevent the file growing unbounded
        if (store[key] === userDataPath) {
            delete store[key];
            break;
        }
    }
    store[sessionID] = userDataPath;
    writeStore(store);
}

export function getProfileFromDeeplink(args): string | undefined {
    // check if we are passed a profile in the SSO callback url
    const deeplinkUrl = args.find(arg => arg.startsWith('element://'));
    if (deeplinkUrl && deeplinkUrl.includes(SEARCH_PARAM)) {
        const parsedUrl = new URL(deeplinkUrl);
        if (parsedUrl.protocol === 'element:') {
            const ssoID = parsedUrl.searchParams.get(SEARCH_PARAM);
            const store = readStore();
            console.log("Forwarding to profile: ", store[ssoID]);
            return store[ssoID];
        }
    }
}

export function protocolInit(): void {
    // get all args except `hidden` as it'd mean the app would not get focused
    // XXX: passing args to protocol handlers only works on Windows, so unpackaged deep-linking
    // --profile/--profile-dir are passed via the SEARCH_PARAM var in the callback url
    const args = process.argv.slice(1).filter(arg => arg !== "--hidden" && arg !== "-hidden");
    if (app.isPackaged) {
        app.setAsDefaultProtocolClient('element', process.execPath, args);
    } else if (process.platform === 'win32') { // on Mac/Linux this would just cause the electron binary to open
        // special handler for running without being packaged, e.g `electron .` by passing our app path to electron
        app.setAsDefaultProtocolClient('element', process.execPath, [app.getAppPath(), ...args]);
    }

    if (process.platform === 'darwin') {
        // Protocol handler for macos
        app.on('open-url', function(ev, url) {
            ev.preventDefault();
            processUrl(url);
        });
    } else {
        // Protocol handler for win32/Linux
        app.on('second-instance', (ev, commandLine) => {
            const url = commandLine[commandLine.length - 1];
            if (!url.startsWith(PROTOCOL)) return;
            processUrl(url);
        });
    }
}
