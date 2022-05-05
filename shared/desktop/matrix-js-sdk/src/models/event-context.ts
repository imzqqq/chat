import { MatrixEvent } from "./event";
import { Direction } from "./event-timeline";

/**
 * @module models/event-context
 */
export class EventContext {
    private timeline: MatrixEvent[];
    private ourEventIndex = 0;
    private paginateTokens: Record<Direction, string | null> = {
        [Direction.Backward]: null,
        [Direction.Forward]: null,
    };

    /**
     * Construct a new EventContext
     *
     * An eventcontext is used for circumstances such as search results, when we
     * have a particular event of interest, and a bunch of events before and after
     * it.
     *
     * It also stores pagination tokens for going backwards and forwards in the
     * timeline.
     *
     * @param {MatrixEvent} ourEvent  the event at the centre of this context
     *
     * @constructor
     */
    constructor(ourEvent: MatrixEvent) {
        this.timeline = [ourEvent];
    }

    /**
     * Get the main event of interest
     *
     * This is a convenience function for getTimeline()[getOurEventIndex()].
     *
     * @return {MatrixEvent} The event at the centre of this context.
     */
    public getEvent(): MatrixEvent {
        return this.timeline[this.ourEventIndex];
    }

    /**
     * Get the list of events in this context
     *
     * @return {Array} An array of MatrixEvents
     */
    public getTimeline(): MatrixEvent[] {
        return this.timeline;
    }

    /**
     * Get the index in the timeline of our event
     *
     * @return {Number}
     */
    public getOurEventIndex(): number {
        return this.ourEventIndex;
    }

    /**
     * Get a pagination token.
     *
     * @param {boolean} backwards   true to get the pagination token for going
     *                                  backwards in time
     * @return {string}
     */
    public getPaginateToken(backwards = false): string {
        return this.paginateTokens[backwards ? Direction.Backward : Direction.Forward];
    }

    /**
     * Set a pagination token.
     *
     * Generally this will be used only by the matrix js sdk.
     *
     * @param {string} token        pagination token
     * @param {boolean} backwards   true to set the pagination token for going
     *                                   backwards in time
     */
    public setPaginateToken(token: string, backwards = false): void {
        this.paginateTokens[backwards ? Direction.Backward : Direction.Forward] = token;
    }

    /**
     * Add more events to the timeline
     *
     * @param {Array} events      new events, in timeline order
     * @param {boolean} atStart   true to insert new events at the start
     */
    public addEvents(events: MatrixEvent[], atStart = false): void {
        // TODO: should we share logic with Room.addEventsToTimeline?
        // Should Room even use EventContext?

        if (atStart) {
            this.timeline = events.concat(this.timeline);
            this.ourEventIndex += events.length;
        } else {
            this.timeline = this.timeline.concat(events);
        }
    }
}
