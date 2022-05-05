import React from 'react';
import * as sdk from '../../../index';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    onDone: () => void;
}

@replaceableComponent("views.verification.VerificationComplete")
export default class VerificationComplete extends React.Component<IProps> {
    public render(): React.ReactNode {
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');
        return <div>
            <h2>{ _t("Verified!") }</h2>
            <p>{ _t("You've successfully verified this user.") }</p>
            <p>{ _t(
                "Secure messages with this user are end-to-end encrypted and not able to be " +
                "read by third parties.",
            ) }</p>
            <DialogButtons onPrimaryButtonClick={this.props.onDone}
                primaryButton={_t("Got It")}
                hasCancel={false}
            />
        </div>;
    }
}
