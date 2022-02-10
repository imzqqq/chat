import { _t } from "../languageHandler";
import Notifier from "../Notifier";
import GenericToast from "../components/views/toasts/GenericToast";
import ToastStore from "../stores/ToastStore";

const onAccept = () => {
    Notifier.setEnabled(true);
};

const onReject = () => {
    Notifier.setPromptHidden(true);
};

const TOAST_KEY = "desktopnotifications";

export const showToast = (fromMessageSend: boolean) => {
    ToastStore.sharedInstance().addOrReplaceToast({
        key: TOAST_KEY,
        title: fromMessageSend ? _t("Don't miss a reply") : _t("Notifications"),
        props: {
            description: _t("Enable desktop notifications"),
            acceptLabel: _t("Enable"),
            onAccept,
            rejectLabel: _t("Dismiss"),
            onReject,
        },
        component: GenericToast,
        priority: 30,
    });
};

export const hideToast = () => {
    ToastStore.sharedInstance().dismissToast(TOAST_KEY);
};
