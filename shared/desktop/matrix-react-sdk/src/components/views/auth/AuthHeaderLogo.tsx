import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.auth.AuthHeaderLogo")
export default class AuthHeaderLogo extends React.PureComponent {
    public render(): React.ReactNode {
        return <div className="mx_AuthHeaderLogo">
            Chat
        </div>;
    }
}
