import React, { useRef, useState } from "react";

import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import Field from "../elements/Field";

interface IProps {
    avatarUrl?: string;
    avatarDisabled?: boolean;
    name?: string;
    nameDisabled?: boolean;
    topic?: string;
    topicDisabled?: boolean;
    setAvatar(avatar: File): void;
    setName(name: string): void;
    setTopic(topic: string): void;
}

export const SpaceAvatar = ({
    avatarUrl,
    avatarDisabled = false,
    setAvatar,
}: Pick<IProps, "avatarUrl" | "avatarDisabled" | "setAvatar">) => {
    const avatarUploadRef = useRef<HTMLInputElement>();
    const [avatar, setAvatarDataUrl] = useState(avatarUrl); // avatar data url cache

    let avatarSection;
    if (avatarDisabled) {
        if (avatar) {
            avatarSection = <img className="mx_SpaceBasicSettings_avatar" src={avatar} alt="" />;
        } else {
            avatarSection = <div className="mx_SpaceBasicSettings_avatar" />;
        }
    } else {
        if (avatar) {
            avatarSection = <React.Fragment>
                <AccessibleButton
                    className="mx_SpaceBasicSettings_avatar"
                    onClick={() => avatarUploadRef.current?.click()}
                    element="img"
                    src={avatar}
                    alt=""
                />
                <AccessibleButton
                    onClick={() => {
                        avatarUploadRef.current.value = "";
                        setAvatarDataUrl(undefined);
                        setAvatar(undefined);
                    }}
                    kind="link"
                    className="mx_SpaceBasicSettings_avatar_remove"
                    aria-label={_t("Delete avatar")}
                >
                    { _t("Delete") }
                </AccessibleButton>
            </React.Fragment>;
        } else {
            avatarSection = <React.Fragment>
                <div className="mx_SpaceBasicSettings_avatar" onClick={() => avatarUploadRef.current?.click()} />
                <AccessibleButton
                    onClick={() => avatarUploadRef.current?.click()}
                    kind="link"
                    aria-label={_t("Upload avatar")}
                >
                    { _t("Upload") }
                </AccessibleButton>
            </React.Fragment>;
        }
    }

    return <div className="mx_SpaceBasicSettings_avatarContainer">
        { avatarSection }
        <input
            type="file"
            ref={avatarUploadRef}
            onChange={(e) => {
                if (!e.target.files?.length) return;
                const file = e.target.files[0];
                setAvatar(file);
                const reader = new FileReader();
                reader.onload = (ev) => {
                    setAvatarDataUrl(ev.target.result as string);
                };
                reader.readAsDataURL(file);
            }}
            accept="image/*"
        />
    </div>;
};

const SpaceBasicSettings = ({
    avatarUrl,
    avatarDisabled = false,
    setAvatar,
    name = "",
    nameDisabled = false,
    setName,
    topic = "",
    topicDisabled = false,
    setTopic,
}: IProps) => {
    return <div className="mx_SpaceBasicSettings">
        <SpaceAvatar avatarUrl={avatarUrl} avatarDisabled={avatarDisabled} setAvatar={setAvatar} />

        <Field
            name="spaceName"
            label={_t("Name")}
            autoFocus={true}
            value={name}
            onChange={ev => setName(ev.target.value)}
            disabled={nameDisabled}
        />

        <Field
            name="spaceTopic"
            element="textarea"
            label={_t("Description")}
            value={topic}
            onChange={ev => setTopic(ev.target.value)}
            rows={3}
            disabled={topicDisabled}
        />
    </div>;
};

export default SpaceBasicSettings;
