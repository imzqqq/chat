import React from 'react';
import { _t } from "../../../../../languageHandler";
import GroupUserSettings from "../../../groups/GroupUserSettings";
import { replaceableComponent } from "../../../../../utils/replaceableComponent";

@replaceableComponent("views.settings.tabs.user.FlairUserSettingsTab")
export default class FlairUserSettingsTab extends React.Component {
    render() {
        return (
            <div className="mx_SettingsTab">
                <span className="mx_SettingsTab_heading">{ _t("Flair") }</span>
                <div className="mx_SettingsTab_section">
                    <GroupUserSettings />
                </div>
            </div>
        );
    }
}
