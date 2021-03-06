import * as React from "react";

import { _t } from '../../../languageHandler';
import { IDialogProps } from "./IDialogProps";
import { useRef, useState } from "react";
import Field from "../elements/Field";
import CountlyAnalytics from "../../../CountlyAnalytics";
import withValidation from "../elements/Validation";
import * as Email from "../../../email";
import BaseDialog from "./BaseDialog";
import DialogButtons from "../elements/DialogButtons";

interface IProps extends IDialogProps {
    onFinished(continued: boolean, email?: string): void;
}

const validation = withValidation({
    rules: [
        {
            key: "email",
            test: ({ value }) => !value || Email.looksValid(value),
            invalid: () => _t("Doesn't look like a valid email address"),
        },
    ],
});

const RegistrationEmailPromptDialog: React.FC<IProps> = ({ onFinished }) => {
    const [email, setEmail] = useState("");
    const fieldRef = useRef<Field>();

    const onSubmit = async (e) => {
        e.preventDefault();
        if (email) {
            const valid = await fieldRef.current.validate({ allowEmpty: false });

            if (!valid) {
                fieldRef.current.focus();
                fieldRef.current.validate({ allowEmpty: false, focused: true });
                return;
            }
        }

        onFinished(true, email);
    };

    return <BaseDialog
        title={_t("Continuing without email")}
        className="mx_RegistrationEmailPromptDialog"
        contentId="mx_RegistrationEmailPromptDialog"
        onFinished={() => onFinished(false)}
        fixedWidth={false}
    >
        <div className="mx_Dialog_content" id="mx_RegistrationEmailPromptDialog">
            <p>{ _t("Just a heads up, if you don't add an email and forget your password, you could " +
                "<b>permanently lose access to your account</b>.", {}, {
                b: sub => <b>{ sub }</b>,
            }) }</p>
            <form onSubmit={onSubmit}>
                <Field
                    ref={fieldRef}
                    autoFocus={true}
                    type="text"
                    label={_t("Email (optional)")}
                    value={email}
                    onChange={ev => {
                        setEmail(ev.target.value);
                    }}
                    onValidate={async fieldState => await validation(fieldState)}
                    onFocus={() => CountlyAnalytics.instance.track("onboarding_registration_email2_focus")}
                    onBlur={() => CountlyAnalytics.instance.track("onboarding_registration_email2_blur")}
                />
            </form>
        </div>
        <DialogButtons
            primaryButton={_t("Continue")}
            onPrimaryButtonClick={onSubmit}
            hasCancel={false}
        />
    </BaseDialog>;
};

export default RegistrationEmailPromptDialog;
