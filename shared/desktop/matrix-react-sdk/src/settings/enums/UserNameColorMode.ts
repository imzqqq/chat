import PropTypes from 'prop-types';

/* TODO: This should be later reworked into something more generic */

export enum UserNameColorMode {
    Uniform = "uniform",
    PowerLevel = "powerlevel",
    MXID = "mxid",
}

/* We need this because multiple components are still using JavaScript */
export const UserNameColorModePropType = PropTypes.oneOf(Object.values(UserNameColorMode));
