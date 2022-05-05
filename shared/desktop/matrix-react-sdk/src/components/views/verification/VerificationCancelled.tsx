import React from 'react';
import * as sdk from '../../../index';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    onDone: () => void;
}

@replaceableComponent("views.verification.VerificationCancelled")
export default class VerificationCancelled extends React.Component<IProps> {
    public render(): React.ReactNode {
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');
        return <div>
            <p>{ _t(
                "The other party cancelled the verification.",
            ) }</p>
            <DialogButtons
                primaryButton={_t('OK')}
                hasCancel={false}
                onPrimaryButtonClick={this.props.onDone}
            />
        </div>;
    }
}
