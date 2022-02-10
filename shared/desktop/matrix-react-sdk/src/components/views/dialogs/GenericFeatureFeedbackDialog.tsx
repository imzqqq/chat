import React, { useState } from "react";

import QuestionDialog from './QuestionDialog';
import { _t } from '../../../languageHandler';
import Field from "../elements/Field";
import SdkConfig from "../../../SdkConfig";
import { IDialogProps } from "./IDialogProps";
import { submitFeedback } from "../../../rageshake/submit-rageshake";
import StyledCheckbox from "../elements/StyledCheckbox";
import Modal from "../../../Modal";
import InfoDialog from "./InfoDialog";

interface IProps extends IDialogProps {
    title: string;
    subheading: string;
    rageshakeLabel: string;
    rageshakeData?: Record<string, string>;
}

const GenericFeatureFeedbackDialog: React.FC<IProps> = ({
    title,
    subheading,
    children,
    rageshakeLabel,
    rageshakeData = {},
    onFinished,
}) => {
    const [comment, setComment] = useState("");
    const [canContact, setCanContact] = useState(false);

    const sendFeedback = async (ok: boolean) => {
        if (!ok) return onFinished(false);

        submitFeedback(SdkConfig.get().bug_report_endpoint_url, rageshakeLabel, comment, canContact, rageshakeData);
        onFinished(true);

        Modal.createTrackedDialog("Feedback Sent", rageshakeLabel, InfoDialog, {
            title,
            description: _t("Thank you for your feedback, we really appreciate it."),
            button: _t("Done"),
            hasCloseButton: false,
            fixedWidth: false,
        });
    };

    return (<QuestionDialog
        className="mx_GenericFeatureFeedbackDialog"
        hasCancelButton={true}
        title={title}
        description={<React.Fragment>
            <div className="mx_GenericFeatureFeedbackDialog_subheading">
                { subheading }
                &nbsp;
                { _t("Your platform and username will be noted to help us use your feedback as much as we can.") }

                { children }
            </div>

            <Field
                id="feedbackComment"
                label={_t("Feedback")}
                type="text"
                autoComplete="off"
                value={comment}
                element="textarea"
                onChange={(ev) => {
                    setComment(ev.target.value);
                }}
                autoFocus={true}
            />

            <StyledCheckbox
                checked={canContact}
                onChange={e => setCanContact((e.target as HTMLInputElement).checked)}
            >
                { _t("You may contact me if you have any follow up questions") }
            </StyledCheckbox>
        </React.Fragment>}
        button={_t("Send feedback")}
        buttonDisabled={!comment}
        onFinished={sendFeedback}
    />);
};

export default GenericFeatureFeedbackDialog;
