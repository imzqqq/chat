import React from 'react';
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import BaseDialog from "./BaseDialog";
import DialogButtons from "../elements/DialogButtons";

interface IProps {
    onFinished: (success: boolean) => void;
}

@replaceableComponent("views.dialogs.ConfirmWipeDeviceDialog")
export default class ConfirmWipeDeviceDialog extends React.Component<IProps> {
    private onConfirm = (): void => {
        this.props.onFinished(true);
    };

    private onDecline = (): void => {
        this.props.onFinished(false);
    };

    render() {
        return (
            <BaseDialog
                className='mx_ConfirmWipeDeviceDialog'
                hasCancel={true}
                onFinished={this.props.onFinished}
                title={_t("Clear all data in this session?")}
            >
                <div className='mx_ConfirmWipeDeviceDialog_content'>
                    <p>
                        { _t(
                            "Clearing all data from this session is permanent. Encrypted messages will be lost " +
                            "unless their keys have been backed up.",
                        ) }
                    </p>
                </div>
                <DialogButtons
                    primaryButton={_t("Clear all data")}
                    onPrimaryButtonClick={this.onConfirm}
                    primaryButtonClass="danger"
                    cancelButton={_t("Cancel")}
                    onCancel={this.onDecline}
                />
            </BaseDialog>
        );
    }
}
