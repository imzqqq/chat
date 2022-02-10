import React from 'react';
import { _t } from '../../../languageHandler';
import * as sdk from '../../../index';
import { SetupEncryptionStore, Phase } from '../../../stores/SetupEncryptionStore';
import SetupEncryptionBody from "./SetupEncryptionBody";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    onFinished: () => void;
}

interface IState {
    phase: Phase;
}

@replaceableComponent("structures.auth.CompleteSecurity")
export default class CompleteSecurity extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        const store = SetupEncryptionStore.sharedInstance();
        store.on("update", this.onStoreUpdate);
        store.start();
        this.state = { phase: store.phase };
    }

    private onStoreUpdate = (): void => {
        const store = SetupEncryptionStore.sharedInstance();
        this.setState({ phase: store.phase });
    };

    public componentWillUnmount(): void {
        const store = SetupEncryptionStore.sharedInstance();
        store.off("update", this.onStoreUpdate);
        store.stop();
    }

    public render() {
        const AuthPage = sdk.getComponent("auth.AuthPage");
        const CompleteSecurityBody = sdk.getComponent("auth.CompleteSecurityBody");
        const { phase } = this.state;
        let icon;
        let title;

        if (phase === Phase.Loading) {
            return null;
        } else if (phase === Phase.Intro) {
            icon = <span className="mx_CompleteSecurity_headerIcon mx_E2EIcon_warning" />;
            title = _t("Verify this login");
        } else if (phase === Phase.Done) {
            icon = <span className="mx_CompleteSecurity_headerIcon mx_E2EIcon_verified" />;
            title = _t("Session verified");
        } else if (phase === Phase.ConfirmSkip) {
            icon = <span className="mx_CompleteSecurity_headerIcon mx_E2EIcon_warning" />;
            title = _t("Are you sure?");
        } else if (phase === Phase.Busy) {
            icon = <span className="mx_CompleteSecurity_headerIcon mx_E2EIcon_warning" />;
            title = _t("Verify this login");
        } else {
            throw new Error(`Unknown phase ${phase}`);
        }

        return (
            <AuthPage>
                <CompleteSecurityBody>
                    <h2 className="mx_CompleteSecurity_header">
                        { icon }
                        { title }
                    </h2>
                    <div className="mx_CompleteSecurity_body">
                        <SetupEncryptionBody onFinished={this.props.onFinished} />
                    </div>
                </CompleteSecurityBody>
            </AuthPage>
        );
    }
}
