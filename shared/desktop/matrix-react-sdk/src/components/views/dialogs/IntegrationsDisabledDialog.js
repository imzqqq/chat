import React from 'react';
import PropTypes from 'prop-types';
import { _t } from "../../../languageHandler";
import * as sdk from "../../../index";
import dis from '../../../dispatcher/dispatcher';
import { Action } from "../../../dispatcher/actions";
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.dialogs.IntegrationsDisabledDialog")
export default class IntegrationsDisabledDialog extends React.Component {
    static propTypes = {
        onFinished: PropTypes.func.isRequired,
    };

    _onAcknowledgeClick = () => {
        this.props.onFinished();
    };

    _onOpenSettingsClick = () => {
        this.props.onFinished();
        dis.fire(Action.ViewUserSettings);
    };

    render() {
        const BaseDialog = sdk.getComponent('views.dialogs.BaseDialog');
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');

        return (
            <BaseDialog
                className='mx_IntegrationsDisabledDialog'
                hasCancel={true}
                onFinished={this.props.onFinished}
                title={_t("Integrations are disabled")}
            >
                <div className='mx_IntegrationsDisabledDialog_content'>
                    <p>{ _t("Enable 'Manage Integrations' in Settings to do this.") }</p>
                </div>
                <DialogButtons
                    primaryButton={_t("Settings")}
                    onPrimaryButtonClick={this._onOpenSettingsClick}
                    cancelButton={_t("OK")}
                    onCancel={this._onAcknowledgeClick}
                />
            </BaseDialog>
        );
    }
}
