import React from 'react';
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";

import BaseDialog from "./BaseDialog";
import DialogButtons from "../elements/DialogButtons";

import { IDialogProps } from "./IDialogProps";

@replaceableComponent("views.dialogs.SeshatResetDialog")
export default class SeshatResetDialog extends React.PureComponent<IDialogProps> {
    render() {
        return (
            <BaseDialog
                hasCancel={true}
                onFinished={this.props.onFinished.bind(null, false)}
                title={_t("Reset event store?")}>
                <div>
                    <p>
                        { _t("You most likely do not want to reset your event index store") }
                        <br />
                        { _t("If you do, please note that none of your messages will be deleted, " +
                            "but the search experience might be degraded for a few moments " +
                            "whilst the index is recreated",
                        ) }
                    </p>
                </div>
                <DialogButtons
                    primaryButton={_t("Reset event store")}
                    onPrimaryButtonClick={this.props.onFinished.bind(null, true)}
                    primaryButtonClass="danger"
                    cancelButton={_t("Cancel")}
                    onCancel={this.props.onFinished.bind(null, false)}
                />
            </BaseDialog>
        );
    }
}
