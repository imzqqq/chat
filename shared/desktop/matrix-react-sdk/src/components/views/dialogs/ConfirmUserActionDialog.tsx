import React from 'react';
import { MatrixClient } from 'matrix-js-sdk/src/client';
import { RoomMember } from "matrix-js-sdk/src/models/room-member";
import { _t } from '../../../languageHandler';
import { GroupMemberType } from '../../../groups';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { mediaFromMxc } from "../../../customisations/Media";
import MemberAvatar from '../avatars/MemberAvatar';
import BaseAvatar from '../avatars/BaseAvatar';
import BaseDialog from "./BaseDialog";
import DialogButtons from "../elements/DialogButtons";

interface IProps {
    // matrix-js-sdk (room) member object. Supply either this or 'groupMember'
    member: RoomMember;
    // group member object. Supply either this or 'member'
    groupMember: GroupMemberType;
    // needed if a group member is specified
    matrixClient?: MatrixClient;
    action: string; // eg. 'Ban'
    title: string; // eg. 'Ban this user?'

    // Whether to display a text field for a reason
    // If true, the second argument to onFinished will
    // be the string entered.
    askReason?: boolean;
    danger?: boolean;
    onFinished: (success: boolean, reason?: string) => void;
}

/*
 * A dialog for confirming an operation on another user.
 * Takes a user ID and a verb, displays the target user prominently
 * such that it should be easy to confirm that the operation is being
 * performed on the right person, and displays the operation prominently
 * to make it obvious what is going to happen.
 * Also tweaks the style for 'dangerous' actions (albeit only with colour)
 */
@replaceableComponent("views.dialogs.ConfirmUserActionDialog")
export default class ConfirmUserActionDialog extends React.Component<IProps> {
    private reasonField: React.RefObject<HTMLInputElement> = React.createRef();

    static defaultProps = {
        danger: false,
        askReason: false,
    };

    public onOk = (): void => {
        this.props.onFinished(true, this.reasonField.current?.value);
    };

    public onCancel = (): void => {
        this.props.onFinished(false);
    };

    public render() {
        const confirmButtonClass = this.props.danger ? 'danger' : '';

        let reasonBox;
        if (this.props.askReason) {
            reasonBox = (
                <div>
                    <form onSubmit={this.onOk}>
                        <input className="mx_ConfirmUserActionDialog_reasonField"
                            ref={this.reasonField}
                            placeholder={_t("Reason")}
                            autoFocus={true}
                        />
                    </form>
                </div>
            );
        }

        let avatar;
        let name;
        let userId;
        if (this.props.member) {
            avatar = <MemberAvatar member={this.props.member} width={48} height={48} />;
            name = this.props.member.name;
            userId = this.props.member.userId;
        } else {
            const httpAvatarUrl = this.props.groupMember.avatarUrl
                ? mediaFromMxc(this.props.groupMember.avatarUrl).getSquareThumbnailHttp(48)
                : null;
            name = this.props.groupMember.displayname || this.props.groupMember.userId;
            userId = this.props.groupMember.userId;
            avatar = <BaseAvatar name={name} url={httpAvatarUrl} width={48} height={48} />;
        }

        return (
            <BaseDialog
                className="mx_ConfirmUserActionDialog"
                onFinished={this.props.onFinished}
                title={this.props.title}
                contentId='mx_Dialog_content'
            >
                <div id="mx_Dialog_content" className="mx_Dialog_content">
                    <div className="mx_ConfirmUserActionDialog_avatar">
                        { avatar }
                    </div>
                    <div className="mx_ConfirmUserActionDialog_name">{ name }</div>
                    <div className="mx_ConfirmUserActionDialog_userId">{ userId }</div>
                </div>
                { reasonBox }
                <DialogButtons primaryButton={this.props.action}
                    onPrimaryButtonClick={this.onOk}
                    primaryButtonClass={confirmButtonClass}
                    focus={!this.props.askReason}
                    onCancel={this.onCancel} />
            </BaseDialog>
        );
    }
}
