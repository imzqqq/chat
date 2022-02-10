import {
    AbortError,
    ConnectionError
} from "./error.js";

function addCacheBuster(urlStr, random = Math.random) {
    // XHR doesn't have a good way to disable cache,
    // so add a random query param
    // see https://davidtranscend.com/blog/prevent-ie11-cache-ajax-requests/
    if (urlStr.includes("?")) {
        urlStr = urlStr + "&";
    } else {
        urlStr = urlStr + "?";
    }
    return urlStr + `_cacheBuster=${Math.ceil(random() * Number.MAX_SAFE_INTEGER)}`;
}

class RequestResult {
    constructor(promise, xhr) {
        this._promise = promise;
        this._xhr = xhr;
    }

    abort() {
        this._xhr.abort();
    }

    response() {
        return this._promise;
    }
}

function createXhr(url, {method, headers, timeout, uploadProgress}) {
    const xhr = new XMLHttpRequest();
    xhr.open(method, url);

    if (headers) {
        for(const [name, value] of headers.entries()) {
            try {
                xhr.setRequestHeader(name, value);
            } catch (err) {
                console.info(`Could not set ${name} header: ${err.message}`);
            }
        }
    }
    if (timeout) {
        xhr.timeout = timeout;
    }

    if (uploadProgress) {
        xhr.upload.addEventListener("progress", evt => uploadProgress(evt.loaded));
    }

    return xhr;
}

function xhrAsPromise(xhr, method, url) {
    return new Promise((resolve, reject) => {
        xhr.addEventListener("load", () => resolve(xhr));
        xhr.addEventListener("abort", () => reject(new AbortError()));
        xhr.addEventListener("error", () => reject(new ConnectionError(`Error ${method} ${url}`)));
        xhr.addEventListener("timeout", () => reject(new ConnectionError(`Timeout ${method} ${url}`, true)));
    });
}

export function xhrRequest(url, options = {}) {
    if (!options.method) {
        options.method = "GET";
    }
    let {cache, body, method} = options;
    if (!cache) {
        url = addCacheBuster(url);
    }
    const xhr = createXhr(url, options);
    const promise = xhrAsPromise(xhr, method, url).then(xhr => {
        const {status} = xhr;
        const body = JSON.parse(xhr.responseText);
        return {status, body};
    });
    
    xhr.send(body || null);

    return new RequestResult(promise, xhr);
}
