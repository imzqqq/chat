import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import AuthFooter from "./AuthFooter";

@replaceableComponent("views.auth.AuthPage")
export default class AuthPage extends React.PureComponent {
    public render(): React.ReactNode {
        return (
            <div className="mx_AuthPage">
                <div className="mx_AuthPage_modal">
                    { this.props.children }
                </div>
                <AuthFooter />
            </div>
        );
    }
}
