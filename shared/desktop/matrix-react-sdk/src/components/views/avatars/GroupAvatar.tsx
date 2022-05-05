import React from 'react';
import { ResizeMethod } from 'matrix-js-sdk/src/@types/partials';

import BaseAvatar from './BaseAvatar';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { mediaFromMxc } from "../../../customisations/Media";

export interface IProps {
        groupId?: string;
        groupName?: string;
        groupAvatarUrl?: string;
        width?: number;
        height?: number;
        resizeMethod?: ResizeMethod;
        onClick?: React.MouseEventHandler;
}

@replaceableComponent("views.avatars.GroupAvatar")
export default class GroupAvatar extends React.Component<IProps> {
    public static defaultProps = {
        width: 36,
        height: 36,
        resizeMethod: 'crop',
    };

    getGroupAvatarUrl() {
        if (!this.props.groupAvatarUrl) return null;
        return mediaFromMxc(this.props.groupAvatarUrl).getThumbnailOfSourceHttp(
            this.props.width,
            this.props.height,
            this.props.resizeMethod,
        );
    }

    render() {
        // extract the props we use from props so we can pass any others through
        // should consider adding this as a global rule in js-sdk?
        /* eslint @typescript-eslint/no-unused-vars: ["error", { "ignoreRestSiblings": true }] */
        const { groupId, groupAvatarUrl, groupName, ...otherProps } = this.props;

        return (
            <BaseAvatar
                name={groupName || this.props.groupId[1]}
                idName={this.props.groupId}
                url={this.getGroupAvatarUrl()}
                {...otherProps}
            />
        );
    }
}
