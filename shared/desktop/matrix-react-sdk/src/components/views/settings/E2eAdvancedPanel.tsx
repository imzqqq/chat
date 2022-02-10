import React from 'react';

import { _t } from "../../../languageHandler";
import { SettingLevel } from "../../../settings/SettingLevel";
import SettingsStore from "../../../settings/SettingsStore";
import SettingsFlag from '../elements/SettingsFlag';

const SETTING_MANUALLY_VERIFY_ALL_SESSIONS = "e2ee.manuallyVerifyAllSessions";

const E2eAdvancedPanel = props => {
    return <div className="mx_SettingsTab_section">
        <span className="mx_SettingsTab_subheading">{ _t("Encryption") }</span>

        <SettingsFlag name={SETTING_MANUALLY_VERIFY_ALL_SESSIONS}
            level={SettingLevel.DEVICE}
        />
        <div className="mx_E2eAdvancedPanel_settingLongDescription">{ _t(
            "Individually verify each session used by a user to mark it as trusted, not trusting cross-signed devices.",
        ) }</div>
    </div>;
};

export default E2eAdvancedPanel;

export function isE2eAdvancedPanelPossible(): boolean {
    return SettingsStore.isEnabled(SETTING_MANUALLY_VERIFY_ALL_SESSIONS);
}
