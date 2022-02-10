import React from "react";

import AccessibleButton from "../../components/views/elements/AccessibleButton";

interface IProps extends React.ComponentProps<typeof AccessibleButton> {
    label?: string;
    // whether or not the context menu is currently open
    isExpanded: boolean;
}

// Semantic component for representing the AccessibleButton which launches a <ContextMenu />
export const ContextMenuButton: React.FC<IProps> = ({
    label,
    isExpanded,
    children,
    onClick,
    onContextMenu,
    ...props
}) => {
    return (
        <AccessibleButton
            {...props}
            onClick={onClick}
            onContextMenu={onContextMenu || onClick}
            title={label}
            aria-label={label}
            aria-haspopup={true}
            aria-expanded={isExpanded}
        >
            { children }
        </AccessibleButton>
    );
};
