import React, { useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";
import { MatrixClient } from "matrix-js-sdk/src/client";
import { EventType } from "matrix-js-sdk/src/@types/event";

import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import SpaceBasicSettings from "./SpaceBasicSettings";
import { avatarUrlForRoom } from "../../../Avatar";
import { IDialogProps } from "../dialogs/IDialogProps";
import { getTopic } from "../elements/RoomTopic";
import { leaveSpace } from "../../../utils/space";

interface IProps extends IDialogProps {
    matrixClient: MatrixClient;
    space: Room;
}

const SpaceSettingsGeneralTab = ({ matrixClient: cli, space, onFinished }: IProps) => {
    const [busy, setBusy] = useState(false);
    const [error, setError] = useState("");

    const userId = cli.getUserId();

    const [newAvatar, setNewAvatar] = useState<File>(null); // undefined means to remove avatar
    const canSetAvatar = space.currentState.maySendStateEvent(EventType.RoomAvatar, userId);
    const avatarChanged = newAvatar !== null;

    const [name, setName] = useState<string>(space.name);
    const canSetName = space.currentState.maySendStateEvent(EventType.RoomName, userId);
    const nameChanged = name !== space.name;

    const currentTopic = getTopic(space);
    const [topic, setTopic] = useState<string>(currentTopic);
    const canSetTopic = space.currentState.maySendStateEvent(EventType.RoomTopic, userId);
    const topicChanged = topic !== currentTopic;

    const onCancel = () => {
        setNewAvatar(null);
        setName(space.name);
        setTopic(currentTopic);
    };

    const onSave = async () => {
        setBusy(true);
        const promises = [];

        if (avatarChanged) {
            if (newAvatar) {
                promises.push(cli.sendStateEvent(space.roomId, EventType.RoomAvatar, {
                    url: await cli.uploadContent(newAvatar),
                }, ""));
            } else {
                promises.push(cli.sendStateEvent(space.roomId, EventType.RoomAvatar, {}, ""));
            }
        }

        if (nameChanged) {
            promises.push(cli.setRoomName(space.roomId, name));
        }

        if (topicChanged) {
            promises.push(cli.setRoomTopic(space.roomId, topic));
        }

        const results = await Promise.allSettled(promises);
        setBusy(false);
        const failures = results.filter(r => r.status === "rejected");
        if (failures.length > 0) {
            console.error("Failed to save space settings: ", failures);
            setError(_t("Failed to save space settings."));
        }
    };

    return <div className="mx_SettingsTab">
        <div className="mx_SettingsTab_heading">{ _t("General") }</div>

        <div>{ _t("Edit settings relating to your space.") }</div>

        { error && <div className="mx_SpaceRoomView_errorText">{ error }</div> }

        <div className="mx_SettingsTab_section">
            <SpaceBasicSettings
                avatarUrl={avatarUrlForRoom(space, 80, 80, "crop")}
                avatarDisabled={busy || !canSetAvatar}
                setAvatar={setNewAvatar}
                name={name}
                nameDisabled={busy || !canSetName}
                setName={setName}
                topic={topic}
                topicDisabled={busy || !canSetTopic}
                setTopic={setTopic}
            />

            <AccessibleButton
                onClick={onCancel}
                disabled={busy || !(avatarChanged || nameChanged || topicChanged)}
                kind="link"
            >
                { _t("Cancel") }
            </AccessibleButton>
            <AccessibleButton onClick={onSave} disabled={busy} kind="primary">
                { busy ? _t("Saving...") : _t("Save Changes") }
            </AccessibleButton>
        </div>

        <span className="mx_SettingsTab_subheading">{ _t("Leave Space") }</span>
        <div className="mx_SettingsTab_section mx_SettingsTab_subsectionText">
            <AccessibleButton
                kind="danger"
                onClick={() => {
                    leaveSpace(space);
                }}
            >
                { _t("Leave Space") }
            </AccessibleButton>
        </div>
    </div>;
};

export default SpaceSettingsGeneralTab;
