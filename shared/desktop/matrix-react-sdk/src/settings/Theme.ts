import PropTypes from 'prop-types';

/* TODO: This should be later reworked into something more generic */

export enum Theme {
    Light = "light",
    Dark = "dark",
    System = "system",
}

/* We need this because multiple components are still using JavaScript */
export const ThemePropType = PropTypes.oneOf(Object.values(Theme));
