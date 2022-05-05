import React from 'react';
import { formatFullDate, formatTime, formatFullTime } from '../../../DateUtils';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    ts: number;
    showTwelveHour?: boolean;
    showFullDate?: boolean;
    showSeconds?: boolean;
}

@replaceableComponent("views.messages.MessageTimestamp")
export default class MessageTimestamp extends React.Component<IProps> {
    public render() {
        const date = new Date(this.props.ts);
        let timestamp;
        if (this.props.showFullDate) {
            timestamp = formatFullDate(date, this.props.showTwelveHour, this.props.showSeconds);
        } else if (this.props.showSeconds) {
            timestamp = formatFullTime(date, this.props.showTwelveHour);
        } else {
            timestamp = formatTime(date, this.props.showTwelveHour);
        }

        return (
            <span
                className="mx_MessageTimestamp"
                title={formatFullDate(date, this.props.showTwelveHour)}
                aria-hidden={true}
            >
                { timestamp }
            </span>
        );
    }
}
