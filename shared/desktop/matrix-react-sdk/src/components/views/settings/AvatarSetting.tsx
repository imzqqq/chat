import React, { useState } from "react";
import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import classNames from "classnames";

interface IProps {
    avatarUrl?: string;
    avatarName: string; // name of user/room the avatar belongs to
    uploadAvatar?: (e: React.MouseEvent) => void;
    removeAvatar?: (e: React.MouseEvent) => void;
    avatarAltText: string;
}

const AvatarSetting: React.FC<IProps> = ({ avatarUrl, avatarAltText, avatarName, uploadAvatar, removeAvatar }) => {
    const [isHovering, setIsHovering] = useState(false);
    const hoveringProps = {
        onMouseEnter: () => setIsHovering(true),
        onMouseLeave: () => setIsHovering(false),
    };

    let avatarElement = <AccessibleButton
        element="div"
        onClick={uploadAvatar}
        className="mx_AvatarSetting_avatarPlaceholder"
        {...hoveringProps}
    />;
    if (avatarUrl) {
        avatarElement = (
            <AccessibleButton
                element="img"
                src={avatarUrl}
                alt={avatarAltText}
                aria-label={avatarAltText}
                onClick={uploadAvatar}
                {...hoveringProps}
            />
        );
    }

    let uploadAvatarBtn;
    if (uploadAvatar) {
        // insert an empty div to be the host for a css mask containing the upload.svg
        uploadAvatarBtn = <AccessibleButton
            onClick={uploadAvatar}
            className='mx_AvatarSetting_uploadButton'
            {...hoveringProps}
        />;
    }

    let removeAvatarBtn;
    if (avatarUrl && removeAvatar) {
        removeAvatarBtn = <AccessibleButton onClick={removeAvatar} kind="link_sm">
            { _t("Remove") }
        </AccessibleButton>;
    }

    const avatarClasses = classNames({
        "mx_AvatarSetting_avatar": true,
        "mx_AvatarSetting_avatar_hovering": isHovering && uploadAvatar,
    });
    return <div className={avatarClasses}>
        { avatarElement }
        <div className="mx_AvatarSetting_hover">
            <div className="mx_AvatarSetting_hoverBg" />
            <span>{ _t("Upload") }</span>
        </div>
        { uploadAvatarBtn }
        { removeAvatarBtn }
    </div>;
};

export default AvatarSetting;
