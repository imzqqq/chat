import React from 'react';
import { _t } from "../../../../../languageHandler";
import { replaceableComponent } from "../../../../../utils/replaceableComponent";
import Notifications from "../../Notifications";

@replaceableComponent("views.settings.tabs.user.NotificationUserSettingsTab")
export default class NotificationUserSettingsTab extends React.Component {
    render() {
        return (
            <div className="mx_SettingsTab mx_NotificationUserSettingsTab">
                <div className="mx_SettingsTab_heading">{ _t("Notifications") }</div>
                <div className="mx_SettingsTab_section mx_SettingsTab_subsectionText">
                    <Notifications />
                </div>
            </div>
        );
    }
}
