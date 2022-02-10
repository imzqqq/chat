import { Dispatch, SetStateAction, useState } from "react";

// Hook to simplify toggling of a boolean state value
// Returns value, method to toggle boolean value and method to set the boolean value
export const useStateToggle = (initialValue = false): [boolean, () => void, Dispatch<SetStateAction<boolean>>] => {
    const [value, setValue] = useState(initialValue);
    const toggleValue = () => {
        setValue(!value);
    };
    return [value, toggleValue, setValue];
};
