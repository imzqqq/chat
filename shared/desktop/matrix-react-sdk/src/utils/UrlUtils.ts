import url from "url";

/**
 * If a url has no path component, etc. abbreviate it to just the hostname
 *
 * @param {string} u The url to be abbreviated
 * @returns {string} The abbreviated url
 */
export function abbreviateUrl(u: string): string {
    if (!u) return '';

    const parsedUrl = url.parse(u);
    // if it's something we can't parse as a url then just return it
    if (!parsedUrl) return u;

    if (parsedUrl.path === '/') {
        // we ignore query / hash parts: these aren't relevant for IS server URLs
        return parsedUrl.host;
    }

    return u;
}

export function unabbreviateUrl(u: string): string {
    if (!u) return '';

    let longUrl = u;
    if (!u.startsWith('https://')) longUrl = 'https://' + u;
    const parsed = url.parse(longUrl);
    if (parsed.hostname === null) return u;

    return longUrl;
}
