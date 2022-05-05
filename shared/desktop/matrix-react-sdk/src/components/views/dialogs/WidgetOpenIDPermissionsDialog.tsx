import React from 'react';
import { _t } from "../../../languageHandler";
import LabelledToggleSwitch from "../elements/LabelledToggleSwitch";
import { Widget, WidgetKind } from "matrix-widget-api";
import { OIDCState, WidgetPermissionStore } from "../../../stores/widgets/WidgetPermissionStore";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { IDialogProps } from "./IDialogProps";
import BaseDialog from "./BaseDialog";
import DialogButtons from "../elements/DialogButtons";

interface IProps extends IDialogProps {
    widget: Widget;
    widgetKind: WidgetKind;
    inRoomId?: string;
}

interface IState {
    rememberSelection: boolean;
}

@replaceableComponent("views.dialogs.WidgetOpenIDPermissionsDialog")
export default class WidgetOpenIDPermissionsDialog extends React.PureComponent<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            rememberSelection: false,
        };
    }

    private onAllow = () => {
        this.onPermissionSelection(true);
    };

    private onDeny = () => {
        this.onPermissionSelection(false);
    };

    private onPermissionSelection(allowed: boolean) {
        if (this.state.rememberSelection) {
            console.log(`Remembering ${this.props.widget.id} as allowed=${allowed} for OpenID`);

            WidgetPermissionStore.instance.setOIDCState(
                this.props.widget, this.props.widgetKind, this.props.inRoomId,
                allowed ? OIDCState.Allowed : OIDCState.Denied,
            );
        }

        this.props.onFinished(allowed);
    }

    private onRememberSelectionChange = (newVal: boolean) => {
        this.setState({ rememberSelection: newVal });
    };

    public render() {
        return (
            <BaseDialog
                className='mx_WidgetOpenIDPermissionsDialog'
                hasCancel={true}
                onFinished={this.props.onFinished}
                title={_t("Allow this widget to verify your identity")}
            >
                <div className='mx_WidgetOpenIDPermissionsDialog_content'>
                    <p>
                        { _t("The widget will verify your user ID, but won't be able to perform actions for you:") }
                    </p>
                    <p className="text-muted">
                        { /* cheap trim to just get the path */ }
                        { this.props.widget.templateUrl.split("?")[0].split("#")[0] }
                    </p>
                </div>
                <DialogButtons
                    primaryButton={_t("Continue")}
                    onPrimaryButtonClick={this.onAllow}
                    onCancel={this.onDeny}
                    additive={
                        <LabelledToggleSwitch
                            value={this.state.rememberSelection}
                            toggleInFront={true}
                            onChange={this.onRememberSelectionChange}
                            label={_t("Remember this")} />}
                />
            </BaseDialog>
        );
    }
}
