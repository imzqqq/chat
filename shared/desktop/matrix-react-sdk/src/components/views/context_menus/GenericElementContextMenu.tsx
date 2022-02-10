import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    element: React.ReactNode;
    // Function to be called when the parent window is resized
    // This can be used to reposition or close the menu on resize and
    // ensure that it is not displayed in a stale position.
    onResize?: () => void;
}

/**
 * This component can be used to display generic HTML content in a contextual
 * menu.
 */
@replaceableComponent("views.context_menus.GenericElementContextMenu")
export default class GenericElementContextMenu extends React.Component<IProps> {
    constructor(props: IProps) {
        super(props);
    }

    public componentDidMount(): void {
        window.addEventListener("resize", this.resize);
    }

    public componentWillUnmount(): void {
        window.removeEventListener("resize", this.resize);
    }

    private resize = (): void => {
        if (this.props.onResize) {
            this.props.onResize();
        }
    };

    public render(): JSX.Chat {
        return <div>{ this.props.element }</div>;
    }
}
