import React from 'react';
import CustomRoomTagStore from '../../stores/CustomRoomTagStore';
import AutoHideScrollbar from './AutoHideScrollbar';
import * as sdk from '../../index';
import dis from '../../dispatcher/dispatcher';
import classNames from 'classnames';
import * as FormattingUtils from '../../utils/FormattingUtils';
import { replaceableComponent } from "../../utils/replaceableComponent";

@replaceableComponent("structures.CustomRoomTagPanel")
class CustomRoomTagPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: CustomRoomTagStore.getSortedTags(),
        };
    }

    componentDidMount() {
        this._tagStoreToken = CustomRoomTagStore.addListener(() => {
            this.setState({ tags: CustomRoomTagStore.getSortedTags() });
        });
    }

    componentWillUnmount() {
        if (this._tagStoreToken) {
            this._tagStoreToken.remove();
        }
    }

    render() {
        const tags = this.state.tags.map((tag) => {
            return (<CustomRoomTagTile tag={tag} key={tag.name} />);
        });

        const classes = classNames('mx_CustomRoomTagPanel', {
            mx_CustomRoomTagPanel_empty: this.state.tags.length === 0,
        });

        return (<div className={classes}>
            <div className="mx_CustomRoomTagPanel_divider" />
            <AutoHideScrollbar className="mx_CustomRoomTagPanel_scroller">
                { tags }
            </AutoHideScrollbar>
        </div>);
    }
}

class CustomRoomTagTile extends React.Component {
    onClick = () => {
        dis.dispatch({ action: 'select_custom_room_tag', tag: this.props.tag.name });
    };

    render() {
        const BaseAvatar = sdk.getComponent('avatars.BaseAvatar');
        const AccessibleTooltipButton = sdk.getComponent('elements.AccessibleTooltipButton');

        const tag = this.props.tag;
        const avatarHeight = 40;
        const className = classNames({
            "CustomRoomTagPanel_tileSelected": tag.selected,
        });
        const name = tag.name;
        const badgeNotifState = tag.badgeNotifState;
        let badgeElement;
        if (badgeNotifState) {
            const badgeClasses = classNames({
                "mx_TagTile_badge": true,
                "mx_TagTile_badgeHighlight": badgeNotifState.hasMentions,
            });
            badgeElement = (<div className={badgeClasses}>{ FormattingUtils.formatCount(badgeNotifState.count) }</div>);
        }

        return (
            <AccessibleTooltipButton className={className} onClick={this.onClick} title={name}>
                <div className="mx_TagTile_avatar">
                    <BaseAvatar
                        name={tag.avatarLetter}
                        idName={name}
                        width={avatarHeight}
                        height={avatarHeight}
                    />
                    { badgeElement }
                </div>
            </AccessibleTooltipButton>
        );
    }
}

export default CustomRoomTagPanel;
