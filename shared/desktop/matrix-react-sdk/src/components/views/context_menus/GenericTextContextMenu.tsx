import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    message: string;
}

@replaceableComponent("views.context_menus.GenericTextContextMenu")
export default class GenericTextContextMenu extends React.Component<IProps> {
    public render(): JSX.Chat {
        return <div>{ this.props.message }</div>;
    }
}
