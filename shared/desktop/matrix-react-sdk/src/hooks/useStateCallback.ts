import { Dispatch, SetStateAction, useState } from "react";

// Hook to simplify interactions with a store-backed state values
// Returns value and method to change the state value
export const useStateCallback = <T>(initialValue: T, callback: (v: T) => void): [T, Dispatch<SetStateAction<T>>] => {
    const [value, setValue] = useState(initialValue);
    const interceptSetValue = (newVal: T) => {
        setValue(newVal);
        callback(newVal);
    };
    return [value, interceptSetValue];
};
