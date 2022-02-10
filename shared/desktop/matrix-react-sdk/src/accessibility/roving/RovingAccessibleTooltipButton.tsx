import React from "react";

import AccessibleTooltipButton from "../../components/views/elements/AccessibleTooltipButton";
import { useRovingTabIndex } from "../RovingTabIndex";
import { Ref } from "./types";

type ATBProps = React.ComponentProps<typeof AccessibleTooltipButton>;
interface IProps extends Omit<ATBProps, "onFocus" | "inputRef" | "tabIndex"> {
    inputRef?: Ref;
}

// Wrapper to allow use of useRovingTabIndex for simple AccessibleTooltipButtons outside of React Functional Components.
export const RovingAccessibleTooltipButton: React.FC<IProps> = ({ inputRef, ...props }) => {
    const [onFocus, isActive, ref] = useRovingTabIndex(inputRef);
    return <AccessibleTooltipButton {...props} onFocus={onFocus} inputRef={ref} tabIndex={isActive ? 0 : -1} />;
};

