import React from "react";

import { IState, RovingTabIndexProvider } from "./RovingTabIndex";
import { Key } from "../Keyboard";

interface IProps extends Omit<React.HTMLProps<HTMLDivElement>, "onKeyDown"> {
}

// This component implements the Toolbar design pattern from the WAI-ARIA Authoring Practices guidelines.
// https://www.w3.org/TR/wai-aria-practices-1.1/#toolbar
// All buttons passed in children must use RovingTabIndex to set `onFocus`, `isActive`, `ref`
const Toolbar: React.FC<IProps> = ({ children, ...props }) => {
    const onKeyDown = (ev: React.KeyboardEvent, state: IState) => {
        const target = ev.target as HTMLElement;
        // Don't interfere with input default keydown behaviour
        if (target.tagName === "INPUT") return;

        let handled = true;

        // HOME and END are handled by RovingTabIndexProvider
        switch (ev.key) {
            case Key.ARROW_UP:
            case Key.ARROW_DOWN:
                if (target.hasAttribute('aria-haspopup')) {
                    target.click();
                }
                break;

            case Key.ARROW_LEFT:
            case Key.ARROW_RIGHT:
                if (state.refs.length > 0) {
                    const i = state.refs.findIndex(r => r === state.activeRef);
                    const delta = ev.key === Key.ARROW_RIGHT ? 1 : -1;
                    state.refs.slice((i + delta) % state.refs.length)[0].current.focus();
                }
                break;

            default:
                handled = false;
        }

        if (handled) {
            ev.preventDefault();
            ev.stopPropagation();
        }
    };

    return <RovingTabIndexProvider handleHomeEnd={true} onKeyDown={onKeyDown}>
        { ({ onKeyDownHandler }) => <div {...props} onKeyDown={onKeyDownHandler} role="toolbar">
            { children }
        </div> }
    </RovingTabIndexProvider>;
};

export default Toolbar;
