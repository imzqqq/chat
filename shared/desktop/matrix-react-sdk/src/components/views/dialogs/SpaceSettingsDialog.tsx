import React, { useMemo } from 'react';
import { Room } from "matrix-js-sdk/src/models/room";
import { MatrixClient } from "matrix-js-sdk/src/client";

import { _t, _td } from '../../../languageHandler';
import { IDialogProps } from "./IDialogProps";
import BaseDialog from "./BaseDialog";
import defaultDispatcher from "../../../dispatcher/dispatcher";
import { useDispatcher } from "../../../hooks/useDispatcher";
import TabbedView, { Tab } from "../../structures/TabbedView";
import SpaceSettingsGeneralTab from '../spaces/SpaceSettingsGeneralTab';
import SpaceSettingsVisibilityTab from "../spaces/SpaceSettingsVisibilityTab";
import SettingsStore from "../../../settings/SettingsStore";
import { UIFeature } from "../../../settings/UIFeature";
import AdvancedRoomSettingsTab from "../settings/tabs/room/AdvancedRoomSettingsTab";
import RolesRoomSettingsTab from "../settings/tabs/room/RolesRoomSettingsTab";

export enum SpaceSettingsTab {
    General = "SPACE_GENERAL_TAB",
    Visibility = "SPACE_VISIBILITY_TAB",
    Roles = "SPACE_ROLES_TAB",
    Advanced = "SPACE_ADVANCED_TAB",
}

interface IProps extends IDialogProps {
    matrixClient: MatrixClient;
    space: Room;
}

const SpaceSettingsDialog: React.FC<IProps> = ({ matrixClient: cli, space, onFinished }) => {
    useDispatcher(defaultDispatcher, ({ action, ...params }) => {
        if (action === "after_leave_room" && params.room_id === space.roomId) {
            onFinished(false);
        }
    });

    const tabs = useMemo(() => {
        return [
            new Tab(
                SpaceSettingsTab.General,
                _td("General"),
                "mx_SpaceSettingsDialog_generalIcon",
                <SpaceSettingsGeneralTab matrixClient={cli} space={space} onFinished={onFinished} />,
            ),
            new Tab(
                SpaceSettingsTab.Visibility,
                _td("Visibility"),
                "mx_SpaceSettingsDialog_visibilityIcon",
                <SpaceSettingsVisibilityTab matrixClient={cli} space={space} closeSettingsFn={onFinished} />,
            ),
            new Tab(
                SpaceSettingsTab.Roles,
                _td("Roles & Permissions"),
                "mx_RoomSettingsDialog_rolesIcon",
                <RolesRoomSettingsTab roomId={space.roomId} />,
            ),
            SettingsStore.getValue(UIFeature.AdvancedSettings)
                ? new Tab(
                    SpaceSettingsTab.Advanced,
                    _td("Advanced"),
                    "mx_RoomSettingsDialog_warningIcon",
                    <AdvancedRoomSettingsTab roomId={space.roomId} closeSettingsFn={onFinished} />,
                )
                : null,
        ].filter(Boolean);
    }, [cli, space, onFinished]);

    return <BaseDialog
        title={_t("Space settings")}
        className="mx_SpaceSettingsDialog"
        contentId="mx_SpaceSettingsDialog"
        onFinished={onFinished}
        fixedWidth={false}
    >
        <div
            className="mx_SpaceSettingsDialog_content"
            id="mx_SpaceSettingsDialog"
            title={_t("Settings - %(spaceName)s", { spaceName: space.name })}
        >
            <TabbedView tabs={tabs} />
        </div>
    </BaseDialog>;
};

export default SpaceSettingsDialog;
