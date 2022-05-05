import React from "react";

import { Key } from "../../Keyboard";
import StyledRadioButton from "../../components/views/elements/StyledRadioButton";

interface IProps extends React.ComponentProps<typeof StyledRadioButton> {
    label?: string;
    onChange(); // we handle keyup/down ourselves so lose the ChangeEvent
    onClose(): void; // gets called after onChange on Key.ENTER
}

// Semantic component for representing a styled role=menuitemradio
export const StyledMenuItemRadio: React.FC<IProps> = ({ children, label, onChange, onClose, ...props }) => {
    const onKeyDown = (e: React.KeyboardEvent) => {
        if (e.key === Key.ENTER || e.key === Key.SPACE) {
            e.stopPropagation();
            e.preventDefault();
            onChange();
            // Implements https://www.w3.org/TR/wai-aria-practices/#keyboard-interaction-12
            if (e.key === Key.ENTER) {
                onClose();
            }
        }
    };
    const onKeyUp = (e: React.KeyboardEvent) => {
        // prevent the input default handler as we handle it on keydown to match
        // https://www.w3.org/TR/wai-aria-practices/examples/menubar/menubar-2/menubar-2.html
        if (e.key === Key.SPACE || e.key === Key.ENTER) {
            e.stopPropagation();
            e.preventDefault();
        }
    };
    return (
        <StyledRadioButton
            {...props}
            role="menuitemradio"
            tabIndex={-1}
            aria-label={label}
            onChange={onChange}
            onKeyDown={onKeyDown}
            onKeyUp={onKeyUp}
        >
            { children }
        </StyledRadioButton>
    );
};
