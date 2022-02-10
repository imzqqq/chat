import React from "react";
import classNames from "classnames";

import {
    ChevronFace,
    ContextMenu,
    IProps as IContextMenuProps,
    MenuItem,
    MenuItemCheckbox, MenuItemRadio,
} from "../../structures/ContextMenu";

interface IProps extends IContextMenuProps {
    className?: string;
    compact?: boolean;
}

interface IOptionListProps {
    first?: boolean;
    red?: boolean;
    className?: string;
}

interface IOptionProps extends React.ComponentProps<typeof MenuItem> {
    iconClassName?: string;
}

interface ICheckboxProps extends React.ComponentProps<typeof MenuItemCheckbox> {
    iconClassName: string;
}

interface IRadioProps extends React.ComponentProps<typeof MenuItemRadio> {
    iconClassName: string;
}

export const IconizedContextMenuRadio: React.FC<IRadioProps> = ({
    label,
    iconClassName,
    active,
    className,
    ...props
}) => {
    return <MenuItemRadio
        {...props}
        className={classNames(className, {
            mx_IconizedContextMenu_active: active,
        })}
        active={active}
        label={label}
    >
        <span className={classNames("mx_IconizedContextMenu_icon", iconClassName)} />
        <span className="mx_IconizedContextMenu_label">{ label }</span>
        { active && <span className="mx_IconizedContextMenu_icon mx_IconizedContextMenu_checked" /> }
    </MenuItemRadio>;
};

export const IconizedContextMenuCheckbox: React.FC<ICheckboxProps> = ({
    label,
    iconClassName,
    active,
    className,
    ...props
}) => {
    return <MenuItemCheckbox
        {...props}
        className={classNames(className, {
            mx_IconizedContextMenu_active: active,
        })}
        active={active}
        label={label}
    >
        <span className={classNames("mx_IconizedContextMenu_icon", iconClassName)} />
        <span className="mx_IconizedContextMenu_label">{ label }</span>
        <span className={classNames("mx_IconizedContextMenu_icon", {
            mx_IconizedContextMenu_checked: active,
            mx_IconizedContextMenu_unchecked: !active,
        })} />
    </MenuItemCheckbox>;
};

export const IconizedContextMenuOption: React.FC<IOptionProps> = ({ label, iconClassName, children, ...props }) => {
    return <MenuItem {...props} label={label}>
        { iconClassName && <span className={classNames("mx_IconizedContextMenu_icon", iconClassName)} /> }
        <span className="mx_IconizedContextMenu_label">{ label }</span>
        { children }
    </MenuItem>;
};

export const IconizedContextMenuOptionList: React.FC<IOptionListProps> = ({ first, red, className, children }) => {
    const classes = classNames("mx_IconizedContextMenu_optionList", className, {
        mx_IconizedContextMenu_optionList_notFirst: !first,
        mx_IconizedContextMenu_optionList_red: red,
    });

    return <div className={classes}>
        { children }
    </div>;
};

const IconizedContextMenu: React.FC<IProps> = ({ className, children, compact, ...props }) => {
    const classes = classNames("mx_IconizedContextMenu", className, {
        mx_IconizedContextMenu_compact: compact,
    });

    return <ContextMenu chevronFace={ChevronFace.None} {...props}>
        <div className={classes}>
            { children }
        </div>
    </ContextMenu>;
};

export default IconizedContextMenu;

