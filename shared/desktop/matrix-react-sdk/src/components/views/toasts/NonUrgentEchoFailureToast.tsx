import React from "react";
import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import Modal from "../../../Modal";
import ServerOfflineDialog from "../dialogs/ServerOfflineDialog";
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.toasts.NonUrgentEchoFailureToast")
export default class NonUrgentEchoFailureToast extends React.PureComponent {
    private openDialog = () => {
        Modal.createTrackedDialog('Local Echo Server Error', '', ServerOfflineDialog, {});
    };

    public render() {
        return (
            <div className="mx_NonUrgentEchoFailureToast">
                <span className="mx_NonUrgentEchoFailureToast_icon" />
                { _t("Your server isn't responding to some <a>requests</a>.", {}, {
                    'a': (sub) => (
                        <AccessibleButton kind="link" onClick={this.openDialog}>{ sub }</AccessibleButton>
                    ),
                }) }
            </div>
        );
    }
}
