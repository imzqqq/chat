export interface ScrollState {
    focussedEvent: string;
    pixelOffset: number;
}

/**
 * Stores where the user has scrolled to in each room
 */
export class RoomScrollStateStore {
    // A map from room id to scroll state.
    //
    // If there is no special scroll state (ie, we are following the live
    // timeline), the scroll state is null. Otherwise, it is an object with
    // the following properties:
    //
    //    focussedEvent: the ID of the 'focussed' event. Typically this is
    //        the last event fully visible in the viewport, though if we
    //        have done an explicit scroll to an explicit event, it will be
    //        that event.
    //
    //    pixelOffset: the number of pixels the window is scrolled down
    //        from the focussedEvent.
    private scrollStateMap = new Map<string, ScrollState>();

    public getScrollState(roomId: string): ScrollState {
        return this.scrollStateMap.get(roomId);
    }

    setScrollState(roomId: string, scrollState: ScrollState): void {
        this.scrollStateMap.set(roomId, scrollState);
    }
}

if (window.mxRoomScrollStateStore === undefined) {
    window.mxRoomScrollStateStore = new RoomScrollStateStore();
}
export default window.mxRoomScrollStateStore;
