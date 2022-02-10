import { _t } from '../../../languageHandler';
import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.auth.AuthFooter")
export default class AuthFooter extends React.Component {
    public render(): React.ReactNode {
        return (
            <div className="mx_AuthFooter">
                <a href="https://matrix.org" target="_blank" rel="noreferrer noopener">{ _t("powered by Chat") }</a>
            </div>
        );
    }
}
