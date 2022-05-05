import React, { ChangeEvent } from 'react';

import { _t } from "../../../../../languageHandler";
import SettingsStore from "../../../../../settings/SettingsStore";
import { SettingLevel } from "../../../../../settings/SettingLevel";
import StyledCheckbox from "../../../elements/StyledCheckbox";
import { useSettingValue } from "../../../../../hooks/useSettings";
import { MetaSpace } from "../../../../../stores/spaces";

export const onMetaSpaceChangeFactory = (metaSpace: MetaSpace) => (e: ChangeEvent<HTMLInputElement>) => {
    const currentValue = SettingsStore.getValue("Spaces.enabledMetaSpaces");
    SettingsStore.setValue("Spaces.enabledMetaSpaces", null, SettingLevel.ACCOUNT, {
        ...currentValue,
        [metaSpace]: e.target.checked,
    });
};

const SidebarUserSettingsTab = () => {
    const {
        [MetaSpace.Home]: homeEnabled,
        [MetaSpace.Favourites]: favouritesEnabled,
        [MetaSpace.People]: peopleEnabled,
        [MetaSpace.Orphans]: orphansEnabled,
    } = useSettingValue<Record<MetaSpace, boolean>>("Spaces.enabledMetaSpaces");
    const allRoomsInHome = useSettingValue<boolean>("Spaces.allRoomsInHome");

    return (
        <div className="mx_SettingsTab mx_SidebarUserSettingsTab">
            <div className="mx_SettingsTab_heading">{ _t("Sidebar") }</div>

            <div className="mx_SettingsTab_section">
                <span className="mx_SettingsTab_subheading">{ _t("Spaces") }</span>
                <div className="mx_SettingsTab_subsectionText">{ _t("Spaces are ways to group rooms and people.") }</div>

                <div className="mx_SidebarUserSettingsTab_subheading">{ _t("Spaces to show") }</div>
                <div className="mx_SettingsTab_subsectionText">
                    { _t("Along with the spaces you're in, you can use some pre-built ones too.") }
                </div>

                <StyledCheckbox
                    checked={!!homeEnabled}
                    onChange={onMetaSpaceChangeFactory(MetaSpace.Home)}
                    className="mx_SidebarUserSettingsTab_homeCheckbox"
                >
                    { _t("Home") }
                </StyledCheckbox>
                <div className="mx_SidebarUserSettingsTab_checkboxMicrocopy">
                    { _t("Home is useful for getting an overview of everything.") }
                </div>

                <StyledCheckbox
                    checked={allRoomsInHome}
                    disabled={!homeEnabled}
                    onChange={e => {
                        SettingsStore.setValue(
                            "Spaces.allRoomsInHome",
                            null,
                            SettingLevel.ACCOUNT,
                            e.target.checked,
                        );
                    }}
                    className="mx_SidebarUserSettingsTab_homeAllRoomsCheckbox"
                >
                    { _t("Show all rooms") }
                </StyledCheckbox>
                <div className="mx_SidebarUserSettingsTab_checkboxMicrocopy">
                    { _t("Show all your rooms in Home, even if they're in a space.") }
                </div>

                <StyledCheckbox
                    checked={!!favouritesEnabled}
                    onChange={onMetaSpaceChangeFactory(MetaSpace.Favourites)}
                    className="mx_SidebarUserSettingsTab_favouritesCheckbox"
                >
                    { _t("Favourites") }
                </StyledCheckbox>
                <div className="mx_SidebarUserSettingsTab_checkboxMicrocopy">
                    { _t("Automatically group all your favourite rooms and people together in one place.") }
                </div>

                <StyledCheckbox
                    checked={!!peopleEnabled}
                    onChange={onMetaSpaceChangeFactory(MetaSpace.People)}
                    className="mx_SidebarUserSettingsTab_peopleCheckbox"
                >
                    { _t("People") }
                </StyledCheckbox>
                <div className="mx_SidebarUserSettingsTab_checkboxMicrocopy">
                    { _t("Automatically group all your people together in one place.") }
                </div>

                <StyledCheckbox
                    checked={!!orphansEnabled}
                    onChange={onMetaSpaceChangeFactory(MetaSpace.Orphans)}
                    className="mx_SidebarUserSettingsTab_orphansCheckbox"
                >
                    { _t("Rooms outside of a space") }
                </StyledCheckbox>
                <div className="mx_SidebarUserSettingsTab_checkboxMicrocopy">
                    { _t("Automatically group all your rooms that aren't part of a space in one place.") }
                </div>
            </div>
        </div>
    );
};

export default SidebarUserSettingsTab;

