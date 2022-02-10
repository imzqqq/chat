import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.auth.CompleteSecurityBody")
export default class CompleteSecurityBody extends React.PureComponent {
    public render(): React.ReactNode {
        return <div className="mx_CompleteSecurityBody">
            { this.props.children }
        </div>;
    }
}
