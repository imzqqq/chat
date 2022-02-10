import React from 'react';

import BaseDialog from "../../../../components/views/dialogs/BaseDialog";
import Spinner from "../../../../components/views/elements/Spinner";
import DialogButtons from "../../../../components/views/elements/DialogButtons";
import dis from "../../../../dispatcher/dispatcher";
import { _t } from '../../../../languageHandler';

import SettingsStore from "../../../../settings/SettingsStore";
import EventIndexPeg from "../../../../indexing/EventIndexPeg";
import { Action } from "../../../../dispatcher/actions";
import { SettingLevel } from "../../../../settings/SettingLevel";
interface IProps {
    onFinished: (success: boolean) => void;
}

interface IState {
    disabling: boolean;
}

/*
 * Allows the user to disable the Event Index.
 */
export default class DisableEventIndexDialog extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            disabling: false,
        };
    }

    private onDisable = async (): Promise<void> => {
        this.setState({
            disabling: true,
        });

        await SettingsStore.setValue('enableEventIndexing', null, SettingLevel.DEVICE, false);
        await EventIndexPeg.deleteEventIndex();
        this.props.onFinished(true);
        dis.fire(Action.ViewUserSettings);
    };

    public render(): React.ReactNode {
        return (
            <BaseDialog onFinished={this.props.onFinished} title={_t("Are you sure?")}>
                { _t("If disabled, messages from encrypted rooms won't appear in search results.") }
                { this.state.disabling ? <Spinner /> : <div /> }
                <DialogButtons
                    primaryButton={_t('Disable')}
                    onPrimaryButtonClick={this.onDisable}
                    primaryButtonClass="danger"
                    cancelButtonClass="warning"
                    onCancel={this.props.onFinished}
                    disabled={this.state.disabling}
                />
            </BaseDialog>
        );
    }
}
