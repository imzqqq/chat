import React from 'react';
import { _t } from '../../../languageHandler';
import AccessibleButton from '../elements/AccessibleButton';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    onScrollUpClick?: (e: React.MouseEvent) => void;
    onCloseClick?: (e: React.MouseEvent) => void;
}

@replaceableComponent("views.rooms.TopUnreadMessagesBar")
export default class TopUnreadMessagesBar extends React.PureComponent<IProps> {
    public render(): JSX.Chat {
        return (
            <div className="mx_TopUnreadMessagesBar">
                <AccessibleButton
                    className="mx_TopUnreadMessagesBar_scrollUp"
                    title={_t('Jump to first unread message.')}
                    onClick={this.props.onScrollUpClick}
                />
                <AccessibleButton
                    className="mx_TopUnreadMessagesBar_markAsRead"
                    title={_t('Mark all as read')}
                    onClick={this.props.onCloseClick}
                />
            </div>
        );
    }
}
