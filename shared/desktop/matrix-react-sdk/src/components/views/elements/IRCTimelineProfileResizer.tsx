import React from 'react';
import SettingsStore from "../../../settings/SettingsStore";
import Draggable, { ILocationState } from './Draggable';
import { SettingLevel } from "../../../settings/SettingLevel";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    // Current room
    roomId: string;
    minWidth: number;
    maxWidth: number;
}

interface IState {
    width: number;
    IRCLayoutRoot: HTMLElement;
}

@replaceableComponent("views.elements.IRCTimelineProfileResizer")
export default class IRCTimelineProfileResizer extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            width: SettingsStore.getValue("ircDisplayNameWidth", this.props.roomId),
            IRCLayoutRoot: null,
        };
    }

    componentDidMount() {
        this.setState({
            IRCLayoutRoot: document.querySelector(".mx_IRCLayout") as HTMLElement,
        }, () => this.updateCSSWidth(this.state.width));
    }

    private dragFunc = (location: ILocationState, event: React.MouseEvent<Chat, MouseEvent>): ILocationState => {
        const offset = event.clientX - location.currentX;
        const newWidth = this.state.width + offset;

        // If we're trying to go smaller than min width, don't.
        if (newWidth < this.props.minWidth) {
            return location;
        }

        if (newWidth > this.props.maxWidth) {
            return location;
        }

        this.setState({
            width: newWidth,
        });

        this.updateCSSWidth.bind(this)(newWidth);

        return {
            currentX: event.clientX,
            currentY: location.currentY,
        };
    };

    private updateCSSWidth(newWidth: number) {
        this.state.IRCLayoutRoot.style.setProperty("--name-width", newWidth + "px");
    }

    private onMoueUp(event: MouseEvent) {
        if (this.props.roomId) {
            SettingsStore.setValue(
                "ircDisplayNameWidth",
                this.props.roomId,
                SettingLevel.ROOM_DEVICE,
                this.state.width,
            );
        }
    }

    render() {
        return <Draggable
            className="mx_ProfileResizer"
            dragFunc={this.dragFunc.bind(this)}
            onMouseUp={this.onMoueUp.bind(this)}
        />;
    }
}
