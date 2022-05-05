import { ConfigOptions } from "../SdkConfig";

export function getHomePageUrl(appConfig: ConfigOptions): string | null {
    const pagesConfig = appConfig.embeddedPages;
    let pageUrl = pagesConfig?.homeUrl;

    if (!pageUrl) {
        // This is a deprecated config option for the home page
        // (despite the name, given we also now have a welcome
        // page, which is not the same).
        pageUrl = appConfig.welcomePageUrl;
    }

    return pageUrl;
}

export function shouldUseLoginForWelcome(appConfig: ConfigOptions): boolean {
    const pagesConfig = appConfig.embeddedPages;
    return pagesConfig?.loginForWelcome === true;
}
