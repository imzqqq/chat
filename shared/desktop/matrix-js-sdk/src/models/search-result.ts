/**
 * @module models/search-result
 */

import { EventContext } from "./event-context";
import { EventMapper } from "../event-mapper";
import { IResultContext, ISearchResult } from "../@types/search";

export class SearchResult {
    /**
     * Create a SearchResponse from the response to /search
     * @static
     * @param {Object} jsonObj
     * @param {function} eventMapper
     * @return {SearchResult}
     */

    public static fromJson(jsonObj: ISearchResult, eventMapper: EventMapper): SearchResult {
        const jsonContext = jsonObj.context || {} as IResultContext;
        const eventsBefore = jsonContext.events_before || [];
        const eventsAfter = jsonContext.events_after || [];

        const context = new EventContext(eventMapper(jsonObj.result));

        context.setPaginateToken(jsonContext.start, true);
        context.addEvents(eventsBefore.map(eventMapper), true);
        context.addEvents(eventsAfter.map(eventMapper), false);
        context.setPaginateToken(jsonContext.end, false);

        return new SearchResult(jsonObj.rank, context);
    }

    /**
     * Construct a new SearchResult
     *
     * @param {number} rank   where this SearchResult ranks in the results
     * @param {event-context.EventContext} context  the matching event and its
     *    context
     *
     * @constructor
     */
    constructor(public readonly rank: number, public readonly context: EventContext) {}
}
