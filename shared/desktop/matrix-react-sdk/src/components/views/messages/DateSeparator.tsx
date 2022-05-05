import React from 'react';

import { _t } from '../../../languageHandler';
import { formatFullDateNoTime } from '../../../DateUtils';
import { replaceableComponent } from "../../../utils/replaceableComponent";

function getDaysArray(): string[] {
    return [
        _t('Sunday'),
        _t('Monday'),
        _t('Tuesday'),
        _t('Wednesday'),
        _t('Thursday'),
        _t('Friday'),
        _t('Saturday'),
    ];
}

interface IProps {
    ts: number;
}

@replaceableComponent("views.messages.DateSeparator")
export default class DateSeparator extends React.Component<IProps> {
    private getLabel() {
        const date = new Date(this.props.ts);
        const today = new Date();
        const yesterday = new Date();
        const days = getDaysArray();
        yesterday.setDate(today.getDate() - 1);

        if (date.toDateString() === today.toDateString()) {
            return _t('Today');
        } else if (date.toDateString() === yesterday.toDateString()) {
            return _t('Yesterday');
        } else if (today.getTime() - date.getTime() < 6 * 24 * 60 * 60 * 1000) {
            return days[date.getDay()];
        } else {
            return formatFullDateNoTime(date);
        }
    }

    render() {
        // ARIA treats <hr/>s as separators, here we abuse them slightly so manually treat this entire thing as one
        // tab-index=-1 to allow it to be focusable but do not add tab stop for it, primarily for screen readers
        return <h2 className="mx_DateSeparator" role="separator" tabIndex={-1}>
            <hr role="none" />
            <div>{ this.getLabel() }</div>
            <hr role="none" />
        </h2>;
    }
}
