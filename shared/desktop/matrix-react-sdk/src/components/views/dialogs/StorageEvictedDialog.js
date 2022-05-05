import React from 'react';
import PropTypes from 'prop-types';
import * as sdk from '../../../index';
import SdkConfig from '../../../SdkConfig';
import Modal from '../../../Modal';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.dialogs.StorageEvictedDialog")
export default class StorageEvictedDialog extends React.Component {
    static propTypes = {
        onFinished: PropTypes.func.isRequired,
    };

    _sendBugReport = ev => {
        ev.preventDefault();
        const BugReportDialog = sdk.getComponent("dialogs.BugReportDialog");
        Modal.createTrackedDialog('Storage evicted', 'Send Bug Report Dialog', BugReportDialog, {});
    };

    _onSignOutClick = () => {
        this.props.onFinished(true);
    };

    render() {
        const BaseDialog = sdk.getComponent('views.dialogs.BaseDialog');
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');

        let logRequest;
        if (SdkConfig.get().bug_report_endpoint_url) {
            logRequest = _t(
                "To help us prevent this in future, please <a>send us logs</a>.",
                {},
                {
                    a: text => <a href="#" onClick={this._sendBugReport}>{ text }</a>,
                },
            );
        }

        return (
            <BaseDialog
                className="mx_ErrorDialog"
                onFinished={this.props.onFinished}
                title={_t('Missing session data')}
                contentId='mx_Dialog_content'
                hasCancel={false}
            >
                <div className="mx_Dialog_content" id='mx_Dialog_content'>
                    <p>{ _t(
                        "Some session data, including encrypted message keys, is " +
                        "missing. Sign out and sign in to fix this, restoring keys " +
                        "from backup.",
                    ) }</p>
                    <p>{ _t(
                        "Your browser likely removed this data when running low on " +
                        "disk space.",
                    ) } { logRequest }</p>
                </div>
                <DialogButtons primaryButton={_t("Sign out")}
                    onPrimaryButtonClick={this._onSignOutClick}
                    focus={true}
                    hasCancel={false}
                />
            </BaseDialog>
        );
    }
}
