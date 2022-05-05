export interface ConfigOptions {
    [key: string]: any;
}

export const DEFAULTS: ConfigOptions = {
    // Brand name of the app
    brand: "Chat",
    // URL to a page we show in an iframe to configure integrations
    integrations_ui_url: "https://scalar.vector.im/",
    // Base URL to the REST interface of the integrations server
    integrations_rest_url: "https://scalar.vector.im/api",
    // Where to send bug reports. If not specified, bugs cannot be sent.
    bug_report_endpoint_url: "https://bugs.imzqqq.top/api/submit",
    // Meet conference options
    jitsi: {
        // Default conference domain
        preferredDomain: "meet.jit.si",
    },
    desktopBuilds: {
        available: true,
        logo: require("../res/img/element-desktop-logo.svg"),
        url: "https://apps.chat.imzqqq.top/desktop",
    },
};

export default class SdkConfig {
    private static instance: ConfigOptions;

    private static setInstance(i: ConfigOptions) {
        SdkConfig.instance = i;

        // For debugging purposes
        (<any>window).mxReactSdkConfig = i;
    }

    static get() {
        return SdkConfig.instance || {};
    }

    static put(cfg: ConfigOptions) {
        const defaultKeys = Object.keys(DEFAULTS);
        for (let i = 0; i < defaultKeys.length; ++i) {
            if (cfg[defaultKeys[i]] === undefined) {
                cfg[defaultKeys[i]] = DEFAULTS[defaultKeys[i]];
            }
        }
        SdkConfig.setInstance(cfg);
    }

    static unset() {
        SdkConfig.setInstance({});
    }

    static add(cfg: ConfigOptions) {
        const liveConfig = SdkConfig.get();
        const newConfig = Object.assign({}, liveConfig, cfg);
        SdkConfig.put(newConfig);
    }
}
