/**
 * Different browsers use different deltaModes. This causes different behaviour.
 * To avoid that we use this function to convert any event to pixels.
 * @param {WheelEvent} event to normalize
 * @returns {WheelEvent} normalized event event
 */
export function normalizeWheelEvent(event: WheelEvent): WheelEvent {
    const LINE_HEIGHT = 18;

    let deltaX;
    let deltaY;
    let deltaZ;

    if (event.deltaMode === 1) { // Units are lines
        deltaX = (event.deltaX * LINE_HEIGHT);
        deltaY = (event.deltaY * LINE_HEIGHT);
        deltaZ = (event.deltaZ * LINE_HEIGHT);
    } else {
        deltaX = event.deltaX;
        deltaY = event.deltaY;
        deltaZ = event.deltaZ;
    }

    return new WheelEvent(
        "syntheticWheel",
        {
            deltaMode: 0,
            deltaY: deltaY,
            deltaX: deltaX,
            deltaZ: deltaZ,
            ...event,
        },
    );
}
