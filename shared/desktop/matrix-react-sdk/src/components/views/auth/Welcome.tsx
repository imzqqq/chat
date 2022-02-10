import React from 'react';
import classNames from "classnames";

import * as sdk from "../../../index";
import SdkConfig from '../../../SdkConfig';
import AuthPage from "./AuthPage";
import { _td } from "../../../languageHandler";
import SettingsStore from "../../../settings/SettingsStore";
import { UIFeature } from "../../../settings/UIFeature";
import CountlyAnalytics from "../../../CountlyAnalytics";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import LanguageSelector from "./LanguageSelector";

// translatable strings for Welcome pages
_td("Sign in with SSO");

interface IProps {

}

@replaceableComponent("views.auth.Welcome")
export default class Welcome extends React.PureComponent<IProps> {
    constructor(props: IProps) {
        super(props);

        CountlyAnalytics.instance.track("onboarding_welcome");
    }

    public render(): React.ReactNode {
        // FIXME: Using an import will result in wrench-element-tests failures
        const EmbeddedPage = sdk.getComponent("structures.EmbeddedPage");

        const pagesConfig = SdkConfig.get().embeddedPages;
        let pageUrl = null;
        if (pagesConfig) {
            pageUrl = pagesConfig.welcomeUrl;
        }
        if (!pageUrl) {
            pageUrl = 'welcome.html';
        }

        return (
            <AuthPage>
                <div className={classNames("mx_Welcome", {
                    mx_WelcomePage_registrationDisabled: !SettingsStore.getValue(UIFeature.Registration),
                })}>
                    <EmbeddedPage
                        className="mx_WelcomePage"
                        url={pageUrl}
                        replaceMap={{
                            "$riot:ssoUrl": "#/start_sso",
                            "$riot:casUrl": "#/start_cas",
                        }}
                    />
                    <LanguageSelector />
                </div>
            </AuthPage>
        );
    }
}
