import React from 'react';
import { JoinRule } from 'matrix-js-sdk/src/@types/partials';

import Dropdown from "./Dropdown";

interface IProps {
    value: JoinRule;
    label: string;
    width?: number;
    labelInvite: string;
    labelPublic: string;
    labelRestricted?: string; // if omitted then this option will be hidden, e.g if unsupported
    onChange(value: JoinRule): void;
}

const JoinRuleDropdown = ({
    label,
    labelInvite,
    labelPublic,
    labelRestricted,
    value,
    width = 448,
    onChange,
}: IProps) => {
    const options = [
        <div key={JoinRule.Invite} className="mx_JoinRuleDropdown_invite">
            { labelInvite }
        </div>,
        <div key={JoinRule.Public} className="mx_JoinRuleDropdown_public">
            { labelPublic }
        </div>,
    ];

    if (labelRestricted) {
        options.unshift(<div key={JoinRule.Restricted} className="mx_JoinRuleDropdown_restricted">
            { labelRestricted }
        </div>);
    }

    return <Dropdown
        id="mx_JoinRuleDropdown"
        className="mx_JoinRuleDropdown"
        onOptionChange={onChange}
        menuWidth={width}
        value={value}
        label={label}
    >
        { options }
    </Dropdown>;
};

export default JoinRuleDropdown;
