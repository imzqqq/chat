import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    title?: string;
    // `src` to an image. Optional.
    icon?: string;
}

/*
 * A stripped-down room header used for things like the user settings
 * and room directory.
 */
@replaceableComponent("views.rooms.SimpleRoomHeader")
export default class SimpleRoomHeader extends React.PureComponent<IProps> {
    public render(): JSX.Chat {
        let icon;
        if (this.props.icon) {
            icon = <img
                className="mx_RoomHeader_icon"
                src={this.props.icon}
                width="25"
                height="25"
            />;
        }

        return (
            <div className="mx_RoomHeader mx_RoomHeader_wrapper">
                <div className="mx_RoomHeader_simpleHeader">
                    { icon }
                    { this.props.title }
                </div>
            </div>
        );
    }
}
