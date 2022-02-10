import React from "react";
import { _t } from '../../../languageHandler';
import AccessibleButton from '../elements/AccessibleButton';
import classNames from 'classnames';

interface IProps {
    numUnreadMessages: number;
    highlight: boolean;
    onScrollToBottomClick: (e: React.MouseEvent) => void;
}

const JumpToBottomButton: React.FC<IProps> = (props) => {
    const className = classNames({
        'mx_JumpToBottomButton': true,
        'mx_JumpToBottomButton_highlight': props.highlight,
    });
    let badge;
    if (props.numUnreadMessages) {
        badge = (<div className="mx_JumpToBottomButton_badge">{ props.numUnreadMessages }</div>);
    }
    return (<div className={className}>
        <AccessibleButton
            className="mx_JumpToBottomButton_scrollDown"
            title={_t("Scroll to most recent messages")}
            onClick={props.onScrollToBottomClick}
        />
        { badge }
    </div>);
};

export default JumpToBottomButton;
