import React from "react";

import { useRovingTabIndex } from "../RovingTabIndex";
import { FocusHandler, Ref } from "./types";

interface IProps {
    inputRef?: Ref;
    children(renderProps: {
        onFocus: FocusHandler;
        isActive: boolean;
        ref: Ref;
    });
}

// Wrapper to allow use of useRovingTabIndex outside of React Functional Components.
export const RovingTabIndexWrapper: React.FC<IProps> = ({ children, inputRef }) => {
    const [onFocus, isActive, ref] = useRovingTabIndex(inputRef);
    return children({ onFocus, isActive, ref });
};
