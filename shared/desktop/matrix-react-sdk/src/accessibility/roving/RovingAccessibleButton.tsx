import React from "react";

import AccessibleButton from "../../components/views/elements/AccessibleButton";
import { useRovingTabIndex } from "../RovingTabIndex";
import { Ref } from "./types";

interface IProps extends Omit<React.ComponentProps<typeof AccessibleButton>, "onFocus" | "inputRef" | "tabIndex"> {
    inputRef?: Ref;
}

// Wrapper to allow use of useRovingTabIndex for simple AccessibleButtons outside of React Functional Components.
export const RovingAccessibleButton: React.FC<IProps> = ({ inputRef, ...props }) => {
    const [onFocus, isActive, ref] = useRovingTabIndex(inputRef);
    return <AccessibleButton {...props} onFocus={onFocus} inputRef={ref} tabIndex={isActive ? 0 : -1} />;
};

