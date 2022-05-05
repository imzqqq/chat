import request from 'browser-request';

// Load the config file. First try to load up a domain-specific config of the
// form "config.$domain.json" and if that fails, fall back to config.json.
export async function getVectorConfig(relativeLocation='') {
    if (relativeLocation !== '' && !relativeLocation.endsWith('/')) relativeLocation += '/';

    const specificConfigPromise = getConfig(`${relativeLocation}config.${document.domain}.json`);
    const generalConfigPromise = getConfig(relativeLocation + "config.json");

    try {
        const configJson = await specificConfigPromise;
        // 404s succeed with an empty json config, so check that there are keys
        if (Object.keys(configJson).length === 0) {
            throw new Error(); // throw to enter the catch
        }
        return configJson;
    } catch (e) {
        return await generalConfigPromise;
    }
}

function getConfig(configJsonFilename: string): Promise<{}> {
    return new Promise(function(resolve, reject) {
        request(
            { method: "GET", url: configJsonFilename, qs: { cachebuster: Date.now() } },
            (err, response, body) => {
                try {
                    if (err || response.status < 200 || response.status >= 300) {
                        // Lack of a config isn't an error, we should
                        // just use the defaults.
                        // Also treat a blank config as no config, assuming
                        // the status code is 0, because we don't get 404s
                        // from file: URIs so this is the only way we can
                        // not fail if the file doesn't exist when loading
                        // from a file:// URI.
                        if (response) {
                            if (response.status == 404 || (response.status == 0 && body == '')) {
                                resolve({});
                            }
                        }
                        reject({ err: err, response: response });
                        return;
                    }

                    // We parse the JSON ourselves rather than use the JSON
                    // parameter, since this throws a parse error on empty
                    // which breaks if there's no config.json and we're
                    // loading from the filesystem (see above).
                    resolve(JSON.parse(body));
                } catch (e) {
                    reject({ err: e });
                }
            },
        );
    });
}
