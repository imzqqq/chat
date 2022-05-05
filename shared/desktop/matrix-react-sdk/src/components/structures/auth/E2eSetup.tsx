import React from 'react';
import AuthPage from '../../views/auth/AuthPage';
import CompleteSecurityBody from '../../views/auth/CompleteSecurityBody';
import CreateCrossSigningDialog from '../../views/dialogs/security/CreateCrossSigningDialog';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    onFinished: () => void;
    accountPassword?: string;
    tokenLogin?: boolean;
}

@replaceableComponent("structures.auth.E2eSetup")
export default class E2eSetup extends React.Component<IProps> {
    render() {
        return (
            <AuthPage>
                <CompleteSecurityBody>
                    <CreateCrossSigningDialog
                        onFinished={this.props.onFinished}
                        accountPassword={this.props.accountPassword}
                        tokenLogin={this.props.tokenLogin}
                    />
                </CompleteSecurityBody>
            </AuthPage>
        );
    }
}
