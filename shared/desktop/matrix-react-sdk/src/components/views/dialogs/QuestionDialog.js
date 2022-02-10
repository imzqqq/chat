import React from 'react';
import PropTypes from 'prop-types';
import classNames from "classnames";

import * as sdk from '../../../index';
import { _t } from '../../../languageHandler';

export default class QuestionDialog extends React.Component {
    static propTypes = {
        title: PropTypes.string,
        description: PropTypes.node,
        extraButtons: PropTypes.node,
        button: PropTypes.string,
        buttonDisabled: PropTypes.bool,
        danger: PropTypes.bool,
        focus: PropTypes.bool,
        onFinished: PropTypes.func.isRequired,
        headerImage: PropTypes.string,
        quitOnly: PropTypes.bool, // quitOnly doesn't show the cancel button just the quit [x].
        fixedWidth: PropTypes.bool,
        className: PropTypes.string,
    };

    static defaultProps = {
        title: "",
        description: "",
        extraButtons: null,
        focus: true,
        hasCancelButton: true,
        danger: false,
        quitOnly: false,
    };

    onOk = () => {
        this.props.onFinished(true);
    };

    onCancel = () => {
        this.props.onFinished(false);
    };

    render() {
        const BaseDialog = sdk.getComponent('views.dialogs.BaseDialog');
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');
        let primaryButtonClass = "";
        if (this.props.danger) {
            primaryButtonClass = "danger";
        }
        return (
            <BaseDialog
                className={classNames("mx_QuestionDialog", this.props.className)}
                onFinished={this.props.onFinished}
                title={this.props.title}
                contentId='mx_Dialog_content'
                headerImage={this.props.headerImage}
                hasCancel={this.props.hasCancelButton}
                fixedWidth={this.props.fixedWidth}
            >
                <div className="mx_Dialog_content" id='mx_Dialog_content'>
                    { this.props.description }
                </div>
                <DialogButtons primaryButton={this.props.button || _t('OK')}
                    primaryButtonClass={primaryButtonClass}
                    primaryDisabled={this.props.buttonDisabled}
                    cancelButton={this.props.cancelButton}
                    hasCancel={this.props.hasCancelButton && !this.props.quitOnly}
                    onPrimaryButtonClick={this.onOk}
                    focus={this.props.focus}
                    onCancel={this.onCancel}
                >
                    { this.props.extraButtons }
                </DialogButtons>
            </BaseDialog>
        );
    }
}
