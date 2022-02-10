import React, { ReactNode } from "react";
import classNames from "classnames";

import StyledRadioButton from "./StyledRadioButton";

export interface IDefinition<T extends string> {
    value: T;
    className?: string;
    disabled?: boolean;
    label: ReactNode;
    description?: ReactNode;
    checked?: boolean; // If provided it will override the value comparison done in the group
}

interface IProps<T extends string> {
    name: string;
    className?: string;
    definitions: IDefinition<T>[];
    value?: T; // if not provided no options will be selected
    outlined?: boolean;
    disabled?: boolean;
    onChange(newValue: T): void;
}

function StyledRadioGroup<T extends string>({
    name,
    definitions,
    value,
    className,
    outlined,
    disabled,
    onChange,
}: IProps<T>) {
    const _onChange = e => {
        onChange(e.target.value);
    };

    return <React.Fragment>
        { definitions.map(d => <React.Fragment key={d.value}>
            <StyledRadioButton
                className={classNames(className, d.className)}
                onChange={_onChange}
                checked={d.checked !== undefined ? d.checked : d.value === value}
                name={name}
                value={d.value}
                disabled={d.disabled ?? disabled}
                outlined={outlined}
            >
                { d.label }
            </StyledRadioButton>
            { d.description ? <span>{ d.description }</span> : null }
        </React.Fragment>) }
    </React.Fragment>;
}

export default StyledRadioGroup;
