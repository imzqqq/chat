import { Dispatch, SetStateAction, useCallback, useEffect, useState } from "react";

const getValue = <T>(key: string, initialValue: T): T => {
    try {
        const item = window.localStorage.getItem(key);
        return item ? JSON.parse(item) : initialValue;
    } catch (error) {
        return initialValue;
    }
};

// Hook behaving like useState but persisting the value to localStorage. Returns same as useState
export const useLocalStorageState = <T>(key: string, initialValue: T): [T, Dispatch<SetStateAction<T>>] => {
    const lsKey = "mx_" + key;

    const [value, setValue] = useState<T>(getValue(lsKey, initialValue));

    useEffect(() => {
        setValue(getValue(lsKey, initialValue));
    }, [lsKey, initialValue]);

    const _setValue: Dispatch<SetStateAction<T>> = useCallback((v: T) => {
        window.localStorage.setItem(lsKey, JSON.stringify(v));
        setValue(v);
    }, [lsKey]);

    return [value, _setValue];
};
