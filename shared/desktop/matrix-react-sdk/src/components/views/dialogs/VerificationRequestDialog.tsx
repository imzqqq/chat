import React from 'react';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { VerificationRequest } from "matrix-js-sdk/src/crypto/verification/request/VerificationRequest";
import BaseDialog from "./BaseDialog";
import EncryptionPanel from "../right_panel/EncryptionPanel";
import { User } from 'matrix-js-sdk/src/models/user';

interface IProps {
    verificationRequest: VerificationRequest;
    verificationRequestPromise: Promise<VerificationRequest>;
    onFinished: () => void;
    member: User;
}

interface IState {
    verificationRequest: VerificationRequest;
}

@replaceableComponent("views.dialogs.VerificationRequestDialog")
export default class VerificationRequestDialog extends React.Component<IProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            verificationRequest: this.props.verificationRequest,
        };
        if (this.props.verificationRequestPromise) {
            this.props.verificationRequestPromise.then(r => {
                this.setState({ verificationRequest: r });
            });
        }
    }

    render() {
        const request = this.state.verificationRequest;
        const otherUserId = request && request.otherUserId;
        const member = this.props.member ||
            otherUserId && MatrixClientPeg.get().getUser(otherUserId);
        const title = request && request.isSelfVerification ?
            _t("Verify other login") : _t("Verification Request");

        return <BaseDialog
            className="mx_InfoDialog"
            onFinished={this.props.onFinished}
            contentId="mx_Dialog_content"
            title={title}
            hasCancel={true}
        >
            <EncryptionPanel
                layout="dialog"
                verificationRequest={this.props.verificationRequest}
                verificationRequestPromise={this.props.verificationRequestPromise}
                onClose={this.props.onFinished}
                member={member}
                isRoomEncrypted={false}
            />
        </BaseDialog>;
    }
}
