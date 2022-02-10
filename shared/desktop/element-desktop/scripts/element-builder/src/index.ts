#!/usr/bin/env node

import logger from './logger';
import DesktopDevelopBuilder from './desktop_develop';
import DesktopReleaseBuilder from './desktop_release';

if (process.env.RIOTBUILD_BASEURL && process.env.RIOTBUILD_ROOMID && process.env.RIOTBUILD_ACCESS_TOKEN) {
    console.log("Logging to console + Chat");
    logger.setup(process.env.RIOTBUILD_BASEURL, process.env.RIOTBUILD_ROOMID, process.env.RIOTBUILD_ACCESS_TOKEN);
} else {
    console.log("No Chat credentials in environment: logging to console only");
}

const winVmName = process.env.RIOTBUILD_WIN_VMNAME;
const winUsername = process.env.RIOTBUILD_WIN_USERNAME;
const winPassword = process.env.RIOTBUILD_WIN_PASSWORD;

const rsyncServer = process.env.RIOTBUILD_RSYNC_ROOT;

if (
    winVmName === undefined ||
    winUsername === undefined ||
    winPassword === undefined
) {
    console.error(
        "No windows credentials set: define RIOTBUILD_WIN_VMNAME, " +
        "RIOTBUILD_WIN_USERNAME and RIOTBUILD_WIN_PASSWORD",
    );
    process.exit(1);
}

if (rsyncServer === undefined) {
    console.error("rsync server not set: define RIOTBUILD_RSYNC_ROOT");
    process.exit(1);
}

// For a release build, this is the tag / branch of element-desktop to build from.
let desktopBranch: string = null;

while (process.argv.length > 2) {
    switch (process.argv[2]) {
        case '--version':
        case '-v':
            process.argv.shift();
            desktopBranch = process.argv[2];
            break;
        default:
            console.error(`Unknown option ${process.argv[2]}`);
            process.exit(1);
    }
    process.argv.shift();
}

let builder;
if (desktopBranch) {
    builder = new DesktopReleaseBuilder(
        winVmName, winUsername, winPassword, rsyncServer, desktopBranch);
} else {
    builder = new DesktopDevelopBuilder(
        winVmName, winUsername, winPassword, rsyncServer);
}
builder.start();
