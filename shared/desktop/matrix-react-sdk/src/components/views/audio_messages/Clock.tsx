import React from "react";
import { formatSeconds } from "../../../DateUtils";
import { replaceableComponent } from "../../../utils/replaceableComponent";

export interface IProps {
    seconds: number;
}

/**
 * Simply converts seconds into minutes and seconds. Note that hours will not be
 * displayed, making it possible to see "82:29".
 */
@replaceableComponent("views.audio_messages.Clock")
export default class Clock extends React.Component<IProps> {
    public constructor(props) {
        super(props);
    }

    shouldComponentUpdate(nextProps: Readonly<IProps>): boolean {
        const currentFloor = Math.floor(this.props.seconds);
        const nextFloor = Math.floor(nextProps.seconds);
        return currentFloor !== nextFloor;
    }

    public render() {
        return <span className='mx_Clock'>{ formatSeconds(this.props.seconds) }</span>;
    }
}
