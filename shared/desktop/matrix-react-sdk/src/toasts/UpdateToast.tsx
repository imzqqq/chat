import React from "react";

import { _t } from "../languageHandler";
import SdkConfig from "../SdkConfig";
import GenericToast from "../components/views/toasts/GenericToast";
import ToastStore from "../stores/ToastStore";
import QuestionDialog from "../components/views/dialogs/QuestionDialog";
import ChangelogDialog from "../components/views/dialogs/ChangelogDialog";
import PlatformPeg from "../PlatformPeg";
import Modal from "../Modal";

const TOAST_KEY = "update";

/*
 * Check a version string is compatible with the Changelog
 * dialog ([element-version]-react-[react-sdk-version]-js-[js-sdk-version])
 */
function checkVersion(ver) {
    const parts = ver.split('-');
    return parts.length === 5 && parts[1] === 'react' && parts[3] === 'js';
}

function installUpdate() {
    PlatformPeg.get().installUpdate();
}

export const showToast = (version: string, newVersion: string, releaseNotes?: string) => {
    function onReject() {
        PlatformPeg.get().deferUpdate(newVersion);
    }

    let onAccept;
    let acceptLabel = _t("What's new?");
    if (releaseNotes) {
        onAccept = () => {
            Modal.createTrackedDialog('Display release notes', '', QuestionDialog, {
                title: _t("What's New"),
                description: <pre>{ releaseNotes }</pre>,
                button: _t("Update"),
                onFinished: (update) => {
                    if (update && PlatformPeg.get()) {
                        PlatformPeg.get().installUpdate();
                    }
                },
            });
        };
    } else if (checkVersion(version) && checkVersion(newVersion)) {
        onAccept = () => {
            Modal.createTrackedDialog('Display Changelog', '', ChangelogDialog, {
                version,
                newVersion,
                onFinished: (update) => {
                    if (update && PlatformPeg.get()) {
                        PlatformPeg.get().installUpdate();
                    }
                },
            });
        };
    } else {
        onAccept = installUpdate;
        acceptLabel = _t("Update");
    }

    const brand = SdkConfig.get().brand;
    ToastStore.sharedInstance().addOrReplaceToast({
        key: TOAST_KEY,
        title: _t("Update %(brand)s", { brand }),
        props: {
            description: _t("New version of %(brand)s is available", { brand }),
            acceptLabel,
            onAccept,
            rejectLabel: _t("Dismiss"),
            onReject,
        },
        component: GenericToast,
        priority: 20,
    });
};

export const hideToast = () => {
    ToastStore.sharedInstance().dismissToast(TOAST_KEY);
};
