import React from 'react';
import PropTypes from 'prop-types';
import { _t } from '../../../languageHandler';
import dis from '../../../dispatcher/dispatcher';
import TagOrderActions from '../../../actions/TagOrderActions';
import { MenuItem } from "../../structures/ContextMenu";
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import GroupFilterOrderStore from "../../../stores/GroupFilterOrderStore";
import { createSpaceFromCommunity } from "../../../utils/space";
import GroupStore from "../../../stores/GroupStore";

@replaceableComponent("views.context_menus.TagTileContextMenu")
export default class TagTileContextMenu extends React.Component {
    static propTypes = {
        tag: PropTypes.string.isRequired,
        index: PropTypes.number.isRequired,
        /* callback called when the menu is dismissed */
        onFinished: PropTypes.func.isRequired,
    };

    static contextType = MatrixClientContext;

    _onViewCommunityClick = () => {
        dis.dispatch({
            action: 'view_group',
            group_id: this.props.tag,
        });
        this.props.onFinished();
    };

    _onRemoveClick = () => {
        dis.dispatch(TagOrderActions.removeTag(this.context, this.props.tag));
        this.props.onFinished();
    };

    _onCreateSpaceClick = () => {
        createSpaceFromCommunity(this.context, this.props.tag);
        this.props.onFinished();
    };

    _onMoveUp = () => {
        dis.dispatch(TagOrderActions.moveTag(this.context, this.props.tag, this.props.index - 1));
        this.props.onFinished();
    };

    _onMoveDown = () => {
        dis.dispatch(TagOrderActions.moveTag(this.context, this.props.tag, this.props.index + 1));
        this.props.onFinished();
    };

    render() {
        let moveUp;
        let moveDown;
        if (this.props.index > 0) {
            moveUp = (
                <MenuItem className="mx_TagTileContextMenu_item mx_TagTileContextMenu_moveUp" onClick={this._onMoveUp}>
                    { _t("Move up") }
                </MenuItem>
            );
        }
        if (this.props.index < (GroupFilterOrderStore.getOrderedTags() || []).length - 1) {
            moveDown = (
                <MenuItem className="mx_TagTileContextMenu_item mx_TagTileContextMenu_moveDown" onClick={this._onMoveDown}>
                    { _t("Move down") }
                </MenuItem>
            );
        }

        let createSpaceOption;
        if (GroupStore.isUserPrivileged(this.props.tag)) {
            createSpaceOption = <>
                <hr className="mx_TagTileContextMenu_separator" role="separator" />
                <MenuItem className="mx_TagTileContextMenu_item mx_TagTileContextMenu_createSpace" onClick={this._onCreateSpaceClick}>
                    { _t("Create Space") }
                </MenuItem>
            </>;
        }

        return <div>
            <MenuItem className="mx_TagTileContextMenu_item mx_TagTileContextMenu_viewCommunity" onClick={this._onViewCommunityClick}>
                { _t('View Community') }
            </MenuItem>
            { (moveUp || moveDown) ? <hr className="mx_TagTileContextMenu_separator" role="separator" /> : null }
            { moveUp }
            { moveDown }
            <hr className="mx_TagTileContextMenu_separator" role="separator" />
            <MenuItem className="mx_TagTileContextMenu_item mx_TagTileContextMenu_hideCommunity" onClick={this._onRemoveClick}>
                { _t("Unpin") }
            </MenuItem>
            { createSpaceOption }
        </div>;
    }
}
