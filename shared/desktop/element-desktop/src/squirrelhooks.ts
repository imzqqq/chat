import path from "path";
import { spawn } from "child_process";
import { app } from "electron";
import { promises as fsProm } from "fs";

function runUpdateExe(args: string[]): Promise<void> {
    // Invokes Squirrel's Update.exe which will do things for us like create shortcuts
    // Note that there's an Update.exe in the app-x.x.x directory and one in the parent
    // directory: we need to run the one in the parent directory, because it discovers
    // information about the app by inspecting the directory it's run from.
    const updateExe = path.resolve(path.dirname(process.execPath), '..', 'Update.exe');
    console.log(`Spawning '${updateExe}' with args '${args}'`);
    return new Promise(resolve => {
        spawn(updateExe, args, {
            detached: true,
        }).on('close', resolve);
    });
}

function checkSquirrelHooks(): boolean {
    if (process.platform !== 'win32') return false;

    const cmd = process.argv[1];
    const target = path.basename(process.execPath);
    if (cmd === '--squirrel-install' || cmd === '--squirrel-updated') {
        runUpdateExe(['--createShortcut=' + target]).then(() => {
            // remove the old 'Classic_Chat' shortcuts, if they exist (update.exe --removeShortcut doesn't work
            // because it always uses the name of the product as the name of the shortcut: the only variable
            // is what executable you're linking to)
            const appDataDir = process.env.APPDATA;
            if (!appDataDir) return;
            const startMenuDir = path.join(
                appDataDir, 'Microsoft', 'Windows', 'Start Menu', 'Programs', 'New Vector Ltd',
            );
            return fsProm.rmdir(startMenuDir, { recursive: true });
        }).then(() => {
            // same for 'Chat (Classic_Chat) which is old now too (we have to try to delete both because
            // we don't know what version we're updating from, but of course we do know this version
            // is 'Chat' so the two old ones are all safe to delete).
            const appDataDir = process.env.APPDATA;
            if (!appDataDir) return;
            const oldStartMenuLink = path.join(
                appDataDir, 'Microsoft', 'Windows', 'Start Menu', 'Programs', 'Chat', 'Chat (Classic_Chat).lnk',
            );
            return fsProm.unlink(oldStartMenuLink).catch(() => {});
        }).then(() => {
            const oldDesktopShortcut = path.join(app.getPath('desktop'), 'Chat (Classic_Chat).lnk');
            return fsProm.unlink(oldDesktopShortcut).catch(() => {});
        }).then(() => {
            const oldDesktopShortcut = path.join(app.getPath('desktop'), 'Classic_Chat.lnk');
            return fsProm.unlink(oldDesktopShortcut).catch(() => {});
        }).then(() => {
            app.quit();
        });
        return true;
    } else if (cmd === '--squirrel-uninstall') {
        runUpdateExe(['--removeShortcut=' + target]).then(() => {
            app.quit();
        });
        return true;
    } else if (cmd === '--squirrel-obsolete') {
        app.quit();
        return true;
    }
    return false;
}

if (checkSquirrelHooks()) {
    process.exit(1);
}
