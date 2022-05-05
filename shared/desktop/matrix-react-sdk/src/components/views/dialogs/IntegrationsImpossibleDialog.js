import React from 'react';
import PropTypes from 'prop-types';
import { _t } from "../../../languageHandler";
import SdkConfig from "../../../SdkConfig";
import * as sdk from "../../../index";
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.dialogs.IntegrationsImpossibleDialog")
export default class IntegrationsImpossibleDialog extends React.Component {
    static propTypes = {
        onFinished: PropTypes.func.isRequired,
    };

    _onAcknowledgeClick = () => {
        this.props.onFinished();
    };

    render() {
        const brand = SdkConfig.get().brand;
        const BaseDialog = sdk.getComponent('views.dialogs.BaseDialog');
        const DialogButtons = sdk.getComponent('views.elements.DialogButtons');

        return (
            <BaseDialog
                className='mx_IntegrationsImpossibleDialog'
                hasCancel={false}
                onFinished={this.props.onFinished}
                title={_t("Integrations not allowed")}
            >
                <div className='mx_IntegrationsImpossibleDialog_content'>
                    <p>
                        { _t(
                            "Your %(brand)s doesn't allow you to use an integration manager to do this. " +
                            "Please contact an admin.",
                            { brand },
                        ) }
                    </p>
                </div>
                <DialogButtons
                    primaryButton={_t("OK")}
                    onPrimaryButtonClick={this._onAcknowledgeClick}
                    hasCancel={false}
                />
            </BaseDialog>
        );
    }
}
