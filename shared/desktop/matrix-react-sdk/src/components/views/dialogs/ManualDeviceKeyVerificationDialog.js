import React from 'react';
import PropTypes from 'prop-types';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import * as sdk from '../../../index';
import * as FormattingUtils from '../../../utils/FormattingUtils';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.dialogs.ManualDeviceKeyVerificationDialog")
export default class ManualDeviceKeyVerificationDialog extends React.Component {
    static propTypes = {
        userId: PropTypes.string.isRequired,
        device: PropTypes.object.isRequired,
        onFinished: PropTypes.func.isRequired,
    };

    _onCancelClick = () => {
        this.props.onFinished(false);
    }

    _onLegacyFinished = (confirm) => {
        if (confirm) {
            MatrixClientPeg.get().setDeviceVerified(
                this.props.userId, this.props.device.deviceId, true,
            );
        }
        this.props.onFinished(confirm);
    }

    render() {
        const QuestionDialog = sdk.getComponent("dialogs.QuestionDialog");

        let text;
        if (MatrixClientPeg.get().getUserId() === this.props.userId) {
            text = _t("Confirm by comparing the following with the User Settings in your other session:");
        } else {
            text = _t("Confirm this user's session by comparing the following with their User Settings:");
        }

        const key = FormattingUtils.formatCryptoKey(this.props.device.getFingerprint());
        const body = (
            <div>
                <p>
                    { text }
                </p>
                <div className="mx_DeviceVerifyDialog_cryptoSection">
                    <ul>
                        <li><label>{ _t("Session name") }:</label> <span>{ this.props.device.getDisplayName() }</span></li>
                        <li><label>{ _t("Session ID") }:</label> <span><code>{ this.props.device.deviceId }</code></span></li>
                        <li><label>{ _t("Session key") }:</label> <span><code><b>{ key }</b></code></span></li>
                    </ul>
                </div>
                <p>
                    { _t("If they don't match, the security of your communication may be compromised.") }
                </p>
            </div>
        );

        return (
            <QuestionDialog
                title={_t("Verify session")}
                description={body}
                button={_t("Verify session")}
                onFinished={this._onLegacyFinished}
            />
        );
    }
}
