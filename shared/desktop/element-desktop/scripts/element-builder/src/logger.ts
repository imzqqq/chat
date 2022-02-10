/* eslint-disable @typescript-eslint/no-explicit-any */

import * as https from 'https';

let baseUrl: string;
let mxAccessToken: string;
let mxRoomId: string;

function setup(matrixServer: string, roomId: string, accessToken: string) {
    baseUrl = matrixServer;
    mxRoomId = roomId;
    mxAccessToken = accessToken;
}

function error(...args: any[]) {
    return log('error', ...args);
}

function warn(...args: any[]) {
    return log('warn', ...args);
}

function info(...args: any[]) {
    return log('info', ...args);
}

function debug(...args: any[]) {
    return log('debug', ...args);
}

type Level = 'error' | 'warn' | 'info' | 'debug';

async function log(level: Level, ...args: any[]) {
    console[level](...args);

    if (baseUrl === undefined) return;

    // log to matrix in the simplest possible way: If it fails, forget it and we lose
    // the log message, and we wait while it completes, so if the server is slow, the
    // build goes slower.
    const ev = {
        msgtype: 'm.notice',
        body: args[0],
    };
    const evData = JSON.stringify(ev);

    const url = baseUrl + "/_matrix/client/r0/rooms/" + encodeURIComponent(mxRoomId) + "/send/m.room.message";
    return new Promise((resolve) => {
        const req = https.request(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + mxAccessToken,
            },
        }, (res) => {
            res.on('end', resolve);
        });
        // Set an error handler even though it's ignored to avoid Node exiting
        // on unhandled errors.
        req.on('error', e => {
            // just ignore for now
        });
        req.write(evData);
        req.end();
    });
}

export default { setup, error, warn, info, debug };
