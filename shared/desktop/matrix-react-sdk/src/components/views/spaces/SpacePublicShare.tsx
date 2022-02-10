import React, { useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";
import { sleep } from "matrix-js-sdk/src/utils";

import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import { copyPlaintext } from "../../../utils/strings";
import { RoomPermalinkCreator } from "../../../utils/permalinks/Permalinks";
import { showRoomInviteDialog } from "../../../RoomInvite";
import { MatrixClientPeg } from "../../../MatrixClientPeg";

interface IProps {
    space: Room;
    onFinished?(): void;
}

const SpacePublicShare = ({ space, onFinished }: IProps) => {
    const [copiedText, setCopiedText] = useState(_t("Click to copy"));

    return <div className="mx_SpacePublicShare">
        <AccessibleButton
            className="mx_SpacePublicShare_shareButton"
            onClick={async () => {
                const permalinkCreator = new RoomPermalinkCreator(space);
                permalinkCreator.load();
                const success = await copyPlaintext(permalinkCreator.forShareableRoom());
                const text = success ? _t("Copied!") : _t("Failed to copy");
                setCopiedText(text);
                await sleep(5000);
                if (copiedText === text) { // if the text hasn't changed by another click then clear it after some time
                    setCopiedText(_t("Click to copy"));
                }
            }}
        >
            <h3>{ _t("Share invite link") }</h3>
            <span>{ copiedText }</span>
        </AccessibleButton>
        { space.canInvite(MatrixClientPeg.get()?.getUserId()) ? <AccessibleButton
            className="mx_SpacePublicShare_inviteButton"
            onClick={() => {
                if (onFinished) onFinished();
                showRoomInviteDialog(space.roomId);
            }}
        >
            <h3>{ _t("Invite people") }</h3>
            <span>{ _t("Invite with email or username") }</span>
        </AccessibleButton> : null }
    </div>;
};

export default SpacePublicShare;
