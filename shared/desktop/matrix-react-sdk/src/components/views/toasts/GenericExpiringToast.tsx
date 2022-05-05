import React from "react";

import ToastStore from "../../../stores/ToastStore";
import GenericToast, { IProps as IGenericToastProps } from "./GenericToast";
import { useExpiringCounter } from "../../../hooks/useTimeout";

interface IProps extends IGenericToastProps {
    toastKey: string;
    numSeconds: number;
    dismissLabel: string;
    onDismiss?();
}

const SECOND = 1000;

const GenericExpiringToast: React.FC<IProps> = ({
    description,
    acceptLabel,
    dismissLabel,
    onAccept,
    onDismiss,
    toastKey,
    numSeconds,
}) => {
    const onReject = () => {
        if (onDismiss) onDismiss();
        ToastStore.sharedInstance().dismissToast(toastKey);
    };
    const counter = useExpiringCounter(onReject, SECOND, numSeconds);

    let rejectLabel = dismissLabel;
    if (counter > 0) {
        rejectLabel += ` (${counter})`;
    }

    return <GenericToast
        description={description}
        acceptLabel={acceptLabel}
        onAccept={onAccept}
        rejectLabel={rejectLabel}
        onReject={onReject}
    />;
};

export default GenericExpiringToast;
