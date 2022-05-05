import React, { ReactNode, KeyboardEvent } from 'react';
import classNames from "classnames";

import { _t } from '../../../languageHandler';
import * as sdk from '../../../index';
import { IDialogProps } from "./IDialogProps";

interface IProps extends IDialogProps {
    title?: string;
    description?: ReactNode;
    className?: string;
    button?: boolean | string;
    hasCloseButton?: boolean;
    fixedWidth?: boolean;
    onKeyDown?(event: KeyboardEvent): void;
}

export default class InfoDialog extends React.Component<IProps> {
    static defaultProps = {
        title: '',
        description: '',
        hasCloseButton: false,
    };

    private onFinished = () => {
        this.props.onFinished();
    };

    render() {
        const BaseDialog = sdk.getComponent('views.dialogs.BaseDialog');
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');
        return (
            <BaseDialog
                className="mx_InfoDialog"
                onFinished={this.props.onFinished}
                title={this.props.title}
                contentId='mx_Dialog_content'
                hasCancel={this.props.hasCloseButton}
                onKeyDown={this.props.onKeyDown}
                fixedWidth={this.props.fixedWidth}
            >
                <div className={classNames("mx_Dialog_content", this.props.className)} id="mx_Dialog_content">
                    { this.props.description }
                </div>
                { this.props.button !== false && <DialogButtons primaryButton={this.props.button || _t('OK')}
                    onPrimaryButtonClick={this.onFinished}
                    hasCancel={false}
                /> }
            </BaseDialog>
        );
    }
}
