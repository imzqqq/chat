import React, { useState } from 'react';
import HostSignupDialog from "../dialogs/HostSignupDialog";
import { HostSignupStore } from "../../../stores/HostSignupStore";
import { useEventEmitter } from "../../../hooks/useEventEmitter";
import { UPDATE_EVENT } from "../../../stores/AsyncStore";

const HostSignupContainer = () => {
    const [isActive, setIsActive] = useState(HostSignupStore.instance.isHostSignupActive);
    useEventEmitter(HostSignupStore.instance, UPDATE_EVENT, () => {
        setIsActive(HostSignupStore.instance.isHostSignupActive);
    });

    return <div className="mx_HostSignupContainer">
        { isActive &&
            <HostSignupDialog />
        }
    </div>;
};

export default HostSignupContainer;
