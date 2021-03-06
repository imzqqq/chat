import { _t } from '../languageHandler';
import dis from "../dispatcher/dispatcher";
import { MatrixClientPeg } from '../MatrixClientPeg';
import DeviceListener from '../DeviceListener';
import ToastStore from "../stores/ToastStore";
import GenericToast from "../components/views/toasts/GenericToast";
import { Action } from "../dispatcher/actions";
import { UserTab } from "../components/views/dialogs/UserSettingsDialog";

function toastKey(deviceId: string) {
    return "unverified_session_" + deviceId;
}

export const showToast = async (deviceId: string) => {
    const cli = MatrixClientPeg.get();

    const onAccept = () => {
        DeviceListener.sharedInstance().dismissUnverifiedSessions([deviceId]);
        dis.dispatch({
            action: Action.ViewUserSettings,
            initialTabId: UserTab.Security,
        });
    };

    const onReject = () => {
        DeviceListener.sharedInstance().dismissUnverifiedSessions([deviceId]);
    };

    const device = await cli.getDevice(deviceId);

    ToastStore.sharedInstance().addOrReplaceToast({
        key: toastKey(deviceId),
        title: _t("New login. Was this you?"),
        icon: "verification_warning",
        props: {
            description: device.display_name,
            detail: _t("%(deviceId)s from %(ip)s", {
                deviceId,
                ip: device.last_seen_ip,
            }),
            acceptLabel: _t("Check your devices"),
            onAccept,
            rejectLabel: _t("Later"),
            onReject,
        },
        component: GenericToast,
        priority: 80,
    });
};

export const hideToast = (deviceId: string) => {
    ToastStore.sharedInstance().dismissToast(toastKey(deviceId));
};
