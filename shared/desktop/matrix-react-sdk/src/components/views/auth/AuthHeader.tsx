import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import AuthHeaderLogo from "./AuthHeaderLogo";
import LanguageSelector from "./LanguageSelector";

interface IProps {
    disableLanguageSelector?: boolean;
}

@replaceableComponent("views.auth.AuthHeader")
export default class AuthHeader extends React.Component<IProps> {
    public render(): React.ReactNode {
        return (
            <div className="mx_AuthHeader">
                <AuthHeaderLogo />
                <LanguageSelector disabled={this.props.disableLanguageSelector} />
            </div>
        );
    }
}
