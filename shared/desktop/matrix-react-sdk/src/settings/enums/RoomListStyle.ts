import PropTypes from 'prop-types';

/* TODO: This should be later reworked into something more generic */

export enum RoomListStyle {
    Compact = "compact",
    Intermediate = "intermediate",
    Roomy = "roomy",
}

/* We need this because multiple components are still using JavaScript */
export const RoomListStylePropType = PropTypes.oneOf(Object.values(RoomListStyle));
