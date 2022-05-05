import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.auth.AuthBody")
export default class AuthBody extends React.PureComponent {
    public render(): React.ReactNode {
        return <div className="mx_AuthBody">
            { this.props.children }
        </div>;
    }
}
