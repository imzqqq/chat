/**
 * @module content-repo
 */

import * as utils from "./utils";

/**
 * Get the HTTP URL for an MXC URI.
 * @param {string} baseUrl The base homeserver url which has a content repo.
 * @param {string} mxc The mxc:// URI.
 * @param {Number} width The desired width of the thumbnail.
 * @param {Number} height The desired height of the thumbnail.
 * @param {string} resizeMethod The thumbnail resize method to use, either
 * "crop" or "scale".
 * @param {Boolean} allowDirectLinks If true, return any non-mxc URLs
 * directly. Fetching such URLs will leak information about the user to
 * anyone they share a room with. If false, will return the emptry string
 * for such URLs.
 * @return {string} The complete URL to the content.
 */
export function getHttpUriForMxc(
    baseUrl: string,
    mxc: string,
    width: number,
    height: number,
    resizeMethod: string,
    allowDirectLinks = false,
): string {
    if (typeof mxc !== "string" || !mxc) {
        return '';
    }
    if (mxc.indexOf("mxc://") !== 0) {
        if (allowDirectLinks) {
            return mxc;
        } else {
            return '';
        }
    }
    let serverAndMediaId = mxc.slice(6); // strips mxc://
    let prefix = "/chat/media/r0/download/";
    const params = {};

    if (width) {
        params["width"] = Math.round(width);
    }
    if (height) {
        params["height"] = Math.round(height);
    }
    if (resizeMethod) {
        params["method"] = resizeMethod;
    }
    if (Object.keys(params).length > 0) {
        // these are thumbnailing params so they probably want the
        // thumbnailing API...
        prefix = "/chat/media/r0/thumbnail/";
    }

    const fragmentOffset = serverAndMediaId.indexOf("#");
    let fragment = "";
    if (fragmentOffset >= 0) {
        fragment = serverAndMediaId.substr(fragmentOffset);
        serverAndMediaId = serverAndMediaId.substr(0, fragmentOffset);
    }

    const urlParams = (Object.keys(params).length === 0 ? "" : ("?" + utils.encodeParams(params)));
    return baseUrl + prefix + serverAndMediaId + urlParams + fragment;
}
