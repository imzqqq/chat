import React from "react";

import AccessibleButton from "../../components/views/elements/AccessibleButton";

interface IProps extends React.ComponentProps<typeof AccessibleButton> {
    label?: string;
    active: boolean;
}

// Semantic component for representing a role=menuitemradio
export const MenuItemRadio: React.FC<IProps> = ({ children, label, active, disabled, ...props }) => {
    return (
        <AccessibleButton
            {...props}
            role="menuitemradio"
            aria-checked={active}
            aria-disabled={disabled}
            disabled={disabled}
            tabIndex={-1}
            aria-label={label}
        >
            { children }
        </AccessibleButton>
    );
};
