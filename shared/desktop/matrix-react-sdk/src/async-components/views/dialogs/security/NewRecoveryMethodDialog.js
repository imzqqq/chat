import React from "react";
import PropTypes from "prop-types";
import * as sdk from "../../../../index";
import { MatrixClientPeg } from '../../../../MatrixClientPeg';
import dis from "../../../../dispatcher/dispatcher";
import { _t } from "../../../../languageHandler";
import Modal from "../../../../Modal";
import RestoreKeyBackupDialog from "../../../../components/views/dialogs/security/RestoreKeyBackupDialog";
import { Action } from "../../../../dispatcher/actions";

export default class NewRecoveryMethodDialog extends React.PureComponent {
    static propTypes = {
        // As returned by js-sdk getKeyBackupVersion()
        newVersionInfo: PropTypes.object,
        onFinished: PropTypes.func.isRequired,
    }

    onOkClick = () => {
        this.props.onFinished();
    }

    onGoToSettingsClick = () => {
        this.props.onFinished();
        dis.fire(Action.ViewUserSettings);
    }

    onSetupClick = async () => {
        Modal.createTrackedDialog(
            'Restore Backup', '', RestoreKeyBackupDialog, {
                onFinished: this.props.onFinished,
            }, null, /* priority = */ false, /* static = */ true,
        );
    }

    render() {
        const BaseDialog = sdk.getComponent("views.dialogs.BaseDialog");
        const DialogButtons = sdk.getComponent("views.elements.DialogButtons");

        const title = <span className="mx_KeyBackupFailedDialog_title">
            { _t("New Recovery Method") }
        </span>;

        const newMethodDetected = <p>{ _t(
            "A new Security Phrase and key for Secure Messages have been detected.",
        ) }</p>;

        const hackWarning = <p className="warning">{ _t(
            "If you didn't set the new recovery method, an " +
            "attacker may be trying to access your account. " +
            "Change your account password and set a new recovery " +
            "method immediately in Settings.",
        ) }</p>;

        let content;
        if (MatrixClientPeg.get().getKeyBackupEnabled()) {
            content = <div>
                { newMethodDetected }
                <p>{ _t(
                    "This session is encrypting history using the new recovery method.",
                ) }</p>
                { hackWarning }
                <DialogButtons
                    primaryButton={_t("OK")}
                    onPrimaryButtonClick={this.onOkClick}
                    cancelButton={_t("Go to Settings")}
                    onCancel={this.onGoToSettingsClick}
                />
            </div>;
        } else {
            content = <div>
                { newMethodDetected }
                { hackWarning }
                <DialogButtons
                    primaryButton={_t("Set up Secure Messages")}
                    onPrimaryButtonClick={this.onSetupClick}
                    cancelButton={_t("Go to Settings")}
                    onCancel={this.onGoToSettingsClick}
                />
            </div>;
        }

        return (
            <BaseDialog className="mx_KeyBackupFailedDialog"
                onFinished={this.props.onFinished}
                title={title}
            >
                { content }
            </BaseDialog>
        );
    }
}
