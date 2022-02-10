/*
 * Separate file that sets up rageshake logging when imported.
 * This is necessary so that rageshake logging is set up before
 * anything else. Webpack puts all import statements at the top
 * of the file before any code, so imports will always be
 * evaluated first. Other imports can cause other code to be
 * evaluated (eg. the loglevel library in js-sdk, which if set
 * up before rageshake causes some js-sdk logging to be missing
 * from the rageshake.)
 */

import * as rageshake from "matrix-react-sdk/src/rageshake/rageshake";
import SdkConfig from "matrix-react-sdk/src/SdkConfig";
import sendBugReport from "matrix-react-sdk/src/rageshake/submit-rageshake";

export function initRageshake() {
    // we manually check persistence for rageshakes ourselves
    const prom = rageshake.init(/*setUpPersistence=*/false);
    prom.then(() => {
        console.log("Initialised rageshake.");
        console.log("To fix line numbers in Chrome: " +
            "Meatball menu → Settings → Ignore list → Add /rageshake\\.js$");

        window.addEventListener('beforeunload', (e) => {
            console.log('element-web closing');
            // try to flush the logs to indexeddb
            rageshake.flush();
        });

        rageshake.cleanup();
    }, (err) => {
        console.error("Failed to initialise rageshake: " + err);
    });
    return prom;
}

export function initRageshakeStore() {
    return rageshake.tryInitStorage();
}

window.mxSendRageshake = function(text: string, withLogs?: boolean) {
    const url = SdkConfig.get().bug_report_endpoint_url;
    if (!url) {
        console.error("Cannot send a rageshake - no bug_report_endpoint_url configured");
        return;
    }

    if (withLogs === undefined) withLogs = true;
    if (!text || !text.trim()) {
        console.error("Cannot send a rageshake without a message - please tell us what went wrong");
        return;
    }
    sendBugReport(url, {
        userText: text,
        sendLogs: withLogs,
        progressCallback: console.log.bind(console),
    }).then(() => {
        console.log("Bug report sent!");
    }, (err) => {
        console.error(err);
    });
};
