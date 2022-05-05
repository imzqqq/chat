import React from "react";

interface IProps extends React.HTMLAttributes<HTMLDivElement> {
    label: string;
}

// Semantic component for representing a role=group for grouping menu radios/checkboxes
export const MenuGroup: React.FC<IProps> = ({ children, label, ...props }) => {
    return <div {...props} role="group" aria-label={label}>
        { children }
    </div>;
};
