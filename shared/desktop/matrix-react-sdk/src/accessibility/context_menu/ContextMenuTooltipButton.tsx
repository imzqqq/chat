import React from "react";

import AccessibleTooltipButton from "../../components/views/elements/AccessibleTooltipButton";

interface IProps extends React.ComponentProps<typeof AccessibleTooltipButton> {
    // whether or not the context menu is currently open
    isExpanded: boolean;
}

// Semantic component for representing the AccessibleButton which launches a <ContextMenu />
export const ContextMenuTooltipButton: React.FC<IProps> = ({
    isExpanded,
    children,
    onClick,
    onContextMenu,
    ...props
}) => {
    return (
        <AccessibleTooltipButton
            {...props}
            onClick={onClick}
            onContextMenu={onContextMenu || onClick}
            aria-haspopup={true}
            aria-expanded={isExpanded}
        >
            { children }
        </AccessibleTooltipButton>
    );
};
