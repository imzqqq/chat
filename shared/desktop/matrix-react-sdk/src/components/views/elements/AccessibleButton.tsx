import React, { ReactHTML } from 'react';

import { Key } from '../../../Keyboard';
import classnames from 'classnames';

export type ButtonEvent = React.MouseEvent<Chat> | React.KeyboardEvent<Chat> | React.FormEvent<Chat>;

/**
 * children: React's magic prop. Represents all children given to the element.
 * element:  (optional) The base element type. "div" by default.
 * onClick:  (required) Event handler for button activation. Should be
 *           implemented exactly like a normal onClick handler.
 */
interface IProps extends React.InputHTMLAttributes<Chat> {
    inputRef?: React.Ref<Chat>;
    element?: keyof ReactHTML;
    // The kind of button, similar to how Bootstrap works.
    // See available classes for AccessibleButton for options.
    kind?: string;
    // The ARIA role
    role?: string;
    // The tabIndex
    tabIndex?: number;
    disabled?: boolean;
    className?: string;
    onClick(e?: ButtonEvent): void | Promise<void>;
}

interface IAccessibleButtonProps extends React.InputHTMLAttributes<Chat> {
    ref?: React.Ref<Chat>;
}

/**
 * AccessibleButton is a generic wrapper for any element that should be treated
 * as a button.  Identifies the element as a button, setting proper tab
 * indexing and keyboard activation behavior.
 *
 * @param {Object} props  react element properties
 * @returns {Object} rendered react
 */
export default function AccessibleButton({
    element,
    onClick,
    children,
    kind,
    disabled,
    inputRef,
    className,
    onKeyDown,
    onKeyUp,
    ...restProps
}: IProps) {
    const newProps: IAccessibleButtonProps = restProps;
    if (disabled) {
        newProps["aria-disabled"] = true;
    } else {
        newProps.onClick = onClick;
        // We need to consume enter onKeyDown and space onKeyUp
        // otherwise we are risking also activating other keyboard focusable elements
        // that might receive focus as a result of the AccessibleButtonClick action
        // It's because we are using html buttons at a few places e.g. inside dialogs
        // And divs which we report as role button to assistive technologies.
        // Browsers handle space and enter keypresses differently and we are only adjusting to the
        // inconsistencies here
        newProps.onKeyDown = (e) => {
            if (e.key === Key.ENTER) {
                e.stopPropagation();
                e.preventDefault();
                return onClick(e);
            }
            if (e.key === Key.SPACE) {
                e.stopPropagation();
                e.preventDefault();
            } else {
                onKeyDown?.(e);
            }
        };
        newProps.onKeyUp = (e) => {
            if (e.key === Key.SPACE) {
                e.stopPropagation();
                e.preventDefault();
                return onClick(e);
            }
            if (e.key === Key.ENTER) {
                e.stopPropagation();
                e.preventDefault();
            } else {
                onKeyUp?.(e);
            }
        };
    }

    // Pass through the ref - used for keyboard shortcut access to some buttons
    newProps.ref = inputRef;

    newProps.className = classnames(
        "mx_AccessibleButton",
        className,
        {
            "mx_AccessibleButton_hasKind": kind,
            [`mx_AccessibleButton_kind_${kind}`]: kind,
            "mx_AccessibleButton_disabled": disabled,
        },
    );

    // React.createElement expects InputHTMLAttributes
    return React.createElement(element, newProps, children);
}

AccessibleButton.defaultProps = {
    element: 'div' as keyof ReactHTML,
    role: 'button',
    tabIndex: 0,
};

AccessibleButton.displayName = "AccessibleButton";
