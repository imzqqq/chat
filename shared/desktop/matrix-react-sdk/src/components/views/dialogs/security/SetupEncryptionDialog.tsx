import React from 'react';
import SetupEncryptionBody from '../../../structures/auth/SetupEncryptionBody';
import BaseDialog from '../BaseDialog';
import { _t } from '../../../../languageHandler';
import { SetupEncryptionStore, Phase } from '../../../../stores/SetupEncryptionStore';
import { replaceableComponent } from "../../../../utils/replaceableComponent";

function iconFromPhase(phase: Phase) {
    if (phase === Phase.Done) {
        return require("../../../../../res/img/e2e/verified.svg");
    } else {
        return require("../../../../../res/img/e2e/warning.svg");
    }
}

interface IProps {
    onFinished: (success: boolean) => void;
}

interface IState {
    icon: Phase;
}

@replaceableComponent("views.dialogs.security.SetupEncryptionDialog")
export default class SetupEncryptionDialog extends React.Component<IProps, IState> {
    private store: SetupEncryptionStore;

    constructor(props: IProps) {
        super(props);

        this.store = SetupEncryptionStore.sharedInstance();
        this.state = { icon: iconFromPhase(this.store.phase) };
    }

    public componentDidMount() {
        this.store.on("update", this.onStoreUpdate);
    }

    public componentWillUnmount() {
        this.store.removeListener("update", this.onStoreUpdate);
    }

    private onStoreUpdate = (): void => {
        this.setState({ icon: iconFromPhase(this.store.phase) });
    };

    public render() {
        return <BaseDialog
            headerImage={this.state.icon}
            onFinished={this.props.onFinished}
            title={_t("Verify this session")}
        >
            <SetupEncryptionBody onFinished={this.props.onFinished} />
        </BaseDialog>;
    }
}
