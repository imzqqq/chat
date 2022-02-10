import React from 'react';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import TextInputDialog from "./TextInputDialog";

interface IProps {
    onFinished: (success: boolean) => void;
}

/*
 * A dialog for confirming a redaction.
 */
@replaceableComponent("views.dialogs.ConfirmRedactDialog")
export default class ConfirmRedactDialog extends React.Component<IProps> {
    render() {
        return (
            <TextInputDialog onFinished={this.props.onFinished}
                title={_t("Confirm Removal")}
                description={
                    _t("Are you sure you wish to remove (delete) this event? " +
                       "Note that if you delete a room name or topic change, it could undo the change.")}
                placeholder={_t("Reason (optional)")}
                focus
                button={_t("Remove")}
            />
        );
    }
}
