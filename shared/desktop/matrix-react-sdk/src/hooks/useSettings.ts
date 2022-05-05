import { useEffect, useState } from "react";
import SettingsStore from '../settings/SettingsStore';

// Hook to fetch the value of a setting and dynamically update when it changes
export const useSettingValue = <T>(settingName: string, roomId: string = null, excludeDefault = false) => {
    const [value, setValue] = useState(SettingsStore.getValue<T>(settingName, roomId, excludeDefault));

    useEffect(() => {
        const ref = SettingsStore.watchSetting(settingName, roomId, () => {
            setValue(SettingsStore.getValue<T>(settingName, roomId, excludeDefault));
        });
        // clean-up
        return () => {
            SettingsStore.unwatchSetting(ref);
        };
    }, [settingName, roomId, excludeDefault]);

    return value;
};

// Hook to fetch whether a feature is enabled and dynamically update when that changes
export const useFeatureEnabled = (featureName: string, roomId: string = null): boolean => {
    const [enabled, setEnabled] = useState(SettingsStore.getValue<boolean>(featureName, roomId));

    useEffect(() => {
        const ref = SettingsStore.watchSetting(featureName, roomId, () => {
            setEnabled(SettingsStore.getValue(featureName, roomId));
        });
        // clean-up
        return () => {
            SettingsStore.unwatchSetting(ref);
        };
    }, [featureName, roomId]);

    return enabled;
};
