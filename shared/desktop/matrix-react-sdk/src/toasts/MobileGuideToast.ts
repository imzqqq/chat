import { _t } from "../languageHandler";
import GenericToast from "../components/views/toasts/GenericToast";
import ToastStore from "../stores/ToastStore";

const onAccept = () => {
    window.location.href = "mobile_guide/";
};

const onReject = () => {
    document.cookie = "element_mobile_redirect_to_guide=false;path=/;max-age=14400";
    hideToast();
};

const TOAST_KEY = "mobileguide";

export const showToast = () => {
    const isIos = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
    const isAndroid = /Android/.test(navigator.userAgent);
    if (!isIos && !isAndroid) {
        return;
    }
    if (document.cookie.includes("element_mobile_redirect_to_guide=false")) {
        return;
    }
    ToastStore.sharedInstance().addOrReplaceToast({
        key: TOAST_KEY,
        title: _t("Use app for a better experience"),
        props: {
            description: _t(
                "Chat Web is experimental on mobile. " +
                "For a better experience and the latest features, use our free native app.",
            ),
            acceptLabel: _t("Use app"),
            onAccept,
            rejectLabel: _t("Dismiss"),
            onReject,
        },
        component: GenericToast,
        priority: 99,
    });
};

export const hideToast = () => {
    ToastStore.sharedInstance().dismissToast(TOAST_KEY);
};
