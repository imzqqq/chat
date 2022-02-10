import React from "react";

import AccessibleButton from "../../components/views/elements/AccessibleButton";
import AccessibleTooltipButton from "../../components/views/elements/AccessibleTooltipButton";

interface IProps extends React.ComponentProps<typeof AccessibleButton> {
    label?: string;
    tooltip?: string;
}

// Semantic component for representing a role=menuitem
export const MenuItem: React.FC<IProps> = ({ children, label, tooltip, ...props }) => {
    const ariaLabel = props["aria-label"] || label;

    if (tooltip) {
        return <AccessibleTooltipButton {...props} role="menuitem" tabIndex={-1} aria-label={ariaLabel} title={tooltip}>
            { children }
        </AccessibleTooltipButton>;
    }

    return (
        <AccessibleButton {...props} role="menuitem" tabIndex={-1} aria-label={ariaLabel}>
            { children }
        </AccessibleButton>
    );
};

