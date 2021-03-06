import { Room } from "matrix-js-sdk/src/models/room";
import { FILTER_CHANGED, FilterKind, IFilterCondition } from "./IFilterCondition";
import { EventEmitter } from "events";
import { normalize } from "matrix-js-sdk/src/utils";
import { throttle } from "lodash";

/**
 * A filter condition for the room list which reveals rooms of a particular
 * name, or associated name (like a room alias).
 */
export class NameFilterCondition extends EventEmitter implements IFilterCondition {
    private _search = "";

    constructor() {
        super();
    }

    public get kind(): FilterKind {
        return FilterKind.Runtime;
    }

    public get search(): string {
        return this._search;
    }

    public set search(val: string) {
        this._search = val;
        this.callUpdate();
    }

    private callUpdate = throttle(() => {
        this.emit(FILTER_CHANGED);
    }, 200, { trailing: true, leading: true });

    public isVisible(room: Room): boolean {
        const lcFilter = this.search.toLowerCase();
        if (this.search[0] === '#') {
            // Try and find rooms by alias
            if (room.getCanonicalAlias() && room.getCanonicalAlias().toLowerCase().startsWith(lcFilter)) {
                return true;
            }
            if (room.getAltAliases().some(a => a.toLowerCase().startsWith(lcFilter))) {
                return true;
            }
        }

        if (!room.name) return false; // should realistically not happen: the js-sdk always calculates a name

        return this.matches(room.normalizedName);
    }

    public matches(normalizedName: string): boolean {
        return normalizedName.includes(normalize(this.search));
    }
}
