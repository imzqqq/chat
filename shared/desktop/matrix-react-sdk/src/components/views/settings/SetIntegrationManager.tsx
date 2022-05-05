import React from 'react';
import { _t } from "../../../languageHandler";
import { IntegrationManagers } from "../../../integrations/IntegrationManagers";
import { IntegrationManagerInstance } from "../../../integrations/IntegrationManagerInstance";
import * as sdk from '../../../index';
import SettingsStore from "../../../settings/SettingsStore";
import { SettingLevel } from "../../../settings/SettingLevel";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {

}

interface IState {
    currentManager: IntegrationManagerInstance;
    provisioningEnabled: boolean;
}

@replaceableComponent("views.settings.SetIntegrationManager")
export default class SetIntegrationManager extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        const currentManager = IntegrationManagers.sharedInstance().getPrimaryManager();

        this.state = {
            currentManager,
            provisioningEnabled: SettingsStore.getValue("integrationProvisioning"),
        };
    }

    private onProvisioningToggled = (): void => {
        const current = this.state.provisioningEnabled;
        SettingsStore.setValue("integrationProvisioning", null, SettingLevel.ACCOUNT, !current).catch(err => {
            console.error("Error changing integration manager provisioning");
            console.error(err);

            this.setState({ provisioningEnabled: current });
        });
        this.setState({ provisioningEnabled: !current });
    };

    public render(): React.ReactNode {
        const ToggleSwitch = sdk.getComponent("views.elements.ToggleSwitch");

        const currentManager = this.state.currentManager;
        let managerName;
        let bodyText;
        if (currentManager) {
            managerName = `(${currentManager.name})`;
            bodyText = _t(
                "Use an integration manager <b>(%(serverName)s)</b> to manage bots, widgets, " +
                "and sticker packs.",
                { serverName: currentManager.name },
                { b: sub => <b>{ sub }</b> },
            );
        } else {
            bodyText = _t("Use an integration manager to manage bots, widgets, and sticker packs.");
        }

        return (
            <div className='mx_SetIntegrationManager'>
                <div className="mx_SettingsTab_heading">
                    <span>{ _t("Manage integrations") }</span>
                    <span className="mx_SettingsTab_subheading">{ managerName }</span>
                    <ToggleSwitch checked={this.state.provisioningEnabled} onChange={this.onProvisioningToggled} />
                </div>
                <span className="mx_SettingsTab_subsectionText">
                    { bodyText }
                    <br />
                    <br />
                    { _t(
                        "Integration managers receive configuration data, and can modify widgets, " +
                        "send room invites, and set power levels on your behalf.",
                    ) }
                </span>
            </div>
        );
    }
}
