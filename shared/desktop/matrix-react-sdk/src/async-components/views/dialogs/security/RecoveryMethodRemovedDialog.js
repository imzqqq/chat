import React from "react";
import PropTypes from "prop-types";
import * as sdk from "../../../../index";
import dis from "../../../../dispatcher/dispatcher";
import { _t } from "../../../../languageHandler";
import Modal from "../../../../Modal";
import { Action } from "../../../../dispatcher/actions";

export default class RecoveryMethodRemovedDialog extends React.PureComponent {
    static propTypes = {
        onFinished: PropTypes.func.isRequired,
    }

    onGoToSettingsClick = () => {
        this.props.onFinished();
        dis.fire(Action.ViewUserSettings);
    }

    onSetupClick = () => {
        this.props.onFinished();
        Modal.createTrackedDialogAsync("Key Backup", "Key Backup",
            import("./CreateKeyBackupDialog"),
            null, null, /* priority = */ false, /* static = */ true,
        );
    }

    render() {
        const BaseDialog = sdk.getComponent("views.dialogs.BaseDialog");
        const DialogButtons = sdk.getComponent("views.elements.DialogButtons");

        const title = <span className="mx_KeyBackupFailedDialog_title">
            { _t("Recovery Method Removed") }
        </span>;

        return (
            <BaseDialog className="mx_KeyBackupFailedDialog"
                onFinished={this.props.onFinished}
                title={title}
            >
                <div>
                    <p>{ _t(
                        "This session has detected that your Security Phrase and key " +
                        "for Secure Messages have been removed.",
                    ) }</p>
                    <p>{ _t(
                        "If you did this accidentally, you can setup Secure Messages on " +
                        "this session which will re-encrypt this session's message " +
                        "history with a new recovery method.",
                    ) }</p>
                    <p className="warning">{ _t(
                        "If you didn't remove the recovery method, an " +
                        "attacker may be trying to access your account. " +
                        "Change your account password and set a new recovery " +
                        "method immediately in Settings.",
                    ) }</p>
                    <DialogButtons
                        primaryButton={_t("Set up Secure Messages")}
                        onPrimaryButtonClick={this.onSetupClick}
                        cancelButton={_t("Go to Settings")}
                        onCancel={this.onGoToSettingsClick}
                    />
                </div>
            </BaseDialog>
        );
    }
}
