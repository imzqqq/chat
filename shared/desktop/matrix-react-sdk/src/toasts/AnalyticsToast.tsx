import React from "react";

import { _t } from "../languageHandler";
import SdkConfig from "../SdkConfig";
import dis from "../dispatcher/dispatcher";
import Analytics from "../Analytics";
import AccessibleButton from "../components/views/elements/AccessibleButton";
import GenericToast from "../components/views/toasts/GenericToast";
import ToastStore from "../stores/ToastStore";

const onAccept = () => {
    dis.dispatch({
        action: 'accept_cookies',
    });
};

const onReject = () => {
    dis.dispatch({
        action: "reject_cookies",
    });
};

const onUsageDataClicked = () => {
    Analytics.showDetailsModal();
};

const TOAST_KEY = "analytics";

export const showToast = (policyUrl?: string) => {
    const brand = SdkConfig.get().brand;
    ToastStore.sharedInstance().addOrReplaceToast({
        key: TOAST_KEY,
        title: _t("Help us improve %(brand)s", { brand }),
        props: {
            description: _t(
                "Send <UsageDataLink>anonymous usage data</UsageDataLink> which helps us improve %(brand)s. " +
                "This will use a <PolicyLink>cookie</PolicyLink>.",
                {
                    brand,
                },
                {
                    "UsageDataLink": (sub) => (
                        <AccessibleButton kind="link" onClick={onUsageDataClicked}>{ sub }</AccessibleButton>
                    ),
                    // XXX: We need to link to the page that explains our cookies
                    "PolicyLink": (sub) => policyUrl ? (
                        <a target="_blank" href={policyUrl}>{ sub }</a>
                    ) : sub,
                },
            ),
            acceptLabel: _t("Yes"),
            onAccept,
            rejectLabel: _t("No"),
            onReject,
        },
        component: GenericToast,
        className: "mx_AnalyticsToast",
        priority: 10,
    });
};

export const hideToast = () => {
    ToastStore.sharedInstance().dismissToast(TOAST_KEY);
};
