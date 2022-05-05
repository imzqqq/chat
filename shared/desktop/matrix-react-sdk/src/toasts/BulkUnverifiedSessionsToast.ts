import { _t } from '../languageHandler';
import dis from "../dispatcher/dispatcher";
import { MatrixClientPeg } from '../MatrixClientPeg';
import DeviceListener from '../DeviceListener';
import GenericToast from "../components/views/toasts/GenericToast";
import ToastStore from "../stores/ToastStore";

const TOAST_KEY = "reviewsessions";

export const showToast = (deviceIds: Set<string>) => {
    const onAccept = () => {
        DeviceListener.sharedInstance().dismissUnverifiedSessions(deviceIds);

        dis.dispatch({
            action: 'view_user_info',
            userId: MatrixClientPeg.get().getUserId(),
        });
    };

    const onReject = () => {
        DeviceListener.sharedInstance().dismissUnverifiedSessions(deviceIds);
    };

    ToastStore.sharedInstance().addOrReplaceToast({
        key: TOAST_KEY,
        title: _t("You have unverified logins"),
        icon: "verification_warning",
        props: {
            description: _t("Review to ensure your account is safe"),
            acceptLabel: _t("Review"),
            onAccept,
            rejectLabel: _t("Later"),
            onReject,
        },
        component: GenericToast,
        priority: 50,
    });
};

export const hideToast = () => {
    ToastStore.sharedInstance().dismissToast(TOAST_KEY);
};
