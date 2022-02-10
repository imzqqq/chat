import React from "react";

import { _t, _td } from "../languageHandler";
import GenericToast from "../components/views/toasts/GenericToast";
import ToastStore from "../stores/ToastStore";
import { messageForResourceLimitError } from "../utils/ErrorUtils";

const TOAST_KEY = "serverlimit";

export const showToast = (limitType: string, onHideToast: () => void, adminContact?: string, syncError?: boolean) => {
    const errorText = messageForResourceLimitError(limitType, adminContact, {
        'monthly_active_user': _td("Your homeserver has exceeded its user limit."),
        'hs_blocked': _td("This homeserver has been blocked by it's administrator."),
        '': _td("Your homeserver has exceeded one of its resource limits."),
    });
    const contactText = messageForResourceLimitError(limitType, adminContact, {
        '': _td("Contact your <a>server admin</a>."),
    });

    ToastStore.sharedInstance().addOrReplaceToast({
        key: TOAST_KEY,
        title: _t("Warning"),
        props: {
            description: <React.Fragment>{ errorText } { contactText }</React.Fragment>,
            acceptLabel: _t("Ok"),
            onAccept: () => {
                hideToast();
                if (onHideToast) onHideToast();
            },
        },
        component: GenericToast,
        priority: 70,
    });
};

export const hideToast = () => {
    ToastStore.sharedInstance().dismissToast(TOAST_KEY);
};
