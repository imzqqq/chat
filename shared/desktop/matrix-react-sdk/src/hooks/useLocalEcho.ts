import { useState } from "react";

export const useLocalEcho = <T extends any>(
    currentFactory: () => T,
    setterFn: (value: T) => Promise<unknown>,
    errorFn: (error: Error) => void,
): [value: T, handler: (value: T) => void] => {
    const [value, setValue] = useState(currentFactory);
    const handler = async (value: T) => {
        setValue(value);
        try {
            await setterFn(value);
        } catch (e) {
            setValue(currentFactory());
            errorFn(e);
        }
    };

    return [value, handler];
};
