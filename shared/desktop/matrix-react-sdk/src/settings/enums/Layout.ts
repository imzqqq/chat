import PropTypes from 'prop-types';

/* TODO: This should be later reworked into something more generic */

export enum Layout {
    IRC = "irc",
    Group = "group",
    Bubble = "bubble",
}

/* We need this because multiple components are still using JavaScript */
export const LayoutPropType = PropTypes.oneOf(Object.values(Layout));
