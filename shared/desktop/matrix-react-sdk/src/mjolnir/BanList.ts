// Inspiration largely taken from Mjolnir itself

import { ListRule, RECOMMENDATION_BAN, recommendationToStable } from "./ListRule";
import { MatrixClientPeg } from "../MatrixClientPeg";

export const RULE_USER = "m.room.rule.user";
export const RULE_ROOM = "m.room.rule.room";
export const RULE_SERVER = "m.room.rule.server";

export const USER_RULE_TYPES = [RULE_USER, "org.matrix.mjolnir.rule.user"];
export const ROOM_RULE_TYPES = [RULE_ROOM, "org.matrix.mjolnir.rule.room"];
export const SERVER_RULE_TYPES = [RULE_SERVER, "org.matrix.mjolnir.rule.server"];
export const ALL_RULE_TYPES = [...USER_RULE_TYPES, ...ROOM_RULE_TYPES, ...SERVER_RULE_TYPES];

export function ruleTypeToStable(rule: string, unstable = true): string {
    if (USER_RULE_TYPES.includes(rule)) {
        return unstable ? USER_RULE_TYPES[USER_RULE_TYPES.length - 1] : RULE_USER;
    }
    if (ROOM_RULE_TYPES.includes(rule)) {
        return unstable ? ROOM_RULE_TYPES[ROOM_RULE_TYPES.length - 1] : RULE_ROOM;
    }
    if (SERVER_RULE_TYPES.includes(rule)) {
        return unstable ? SERVER_RULE_TYPES[SERVER_RULE_TYPES.length - 1] : RULE_SERVER;
    }
    return null;
}

export class BanList {
    _rules: ListRule[] = [];
    _roomId: string;

    constructor(roomId: string) {
        this._roomId = roomId;
        this.updateList();
    }

    get roomId(): string {
        return this._roomId;
    }

    get serverRules(): ListRule[] {
        return this._rules.filter(r => r.kind === RULE_SERVER);
    }

    get userRules(): ListRule[] {
        return this._rules.filter(r => r.kind === RULE_USER);
    }

    get roomRules(): ListRule[] {
        return this._rules.filter(r => r.kind === RULE_ROOM);
    }

    async banEntity(kind: string, entity: string, reason: string): Promise<any> {
        await MatrixClientPeg.get().sendStateEvent(this._roomId, ruleTypeToStable(kind, true), {
            entity: entity,
            reason: reason,
            recommendation: recommendationToStable(RECOMMENDATION_BAN, true),
        }, "rule:" + entity);
        this._rules.push(new ListRule(entity, RECOMMENDATION_BAN, reason, ruleTypeToStable(kind, false)));
    }

    async unbanEntity(kind: string, entity: string): Promise<any> {
        // Empty state event is effectively deleting it.
        await MatrixClientPeg.get().sendStateEvent(this._roomId, ruleTypeToStable(kind, true), {}, "rule:" + entity);
        this._rules = this._rules.filter(r => {
            if (r.kind !== ruleTypeToStable(kind, false)) return true;
            if (r.entity !== entity) return true;
            return false; // we just deleted this rule
        });
    }

    updateList() {
        this._rules = [];

        const room = MatrixClientPeg.get().getRoom(this._roomId);
        if (!room) return;

        for (const eventType of ALL_RULE_TYPES) {
            const events = room.currentState.getStateEvents(eventType);
            for (const ev of events) {
                if (!ev.getStateKey()) continue;

                const kind = ruleTypeToStable(eventType, false);

                const entity = ev.getContent()['entity'];
                const recommendation = ev.getContent()['recommendation'];
                const reason = ev.getContent()['reason'];
                if (!entity || !recommendation || !reason) continue;

                this._rules.push(new ListRule(entity, recommendation, reason, kind));
            }
        }
    }
}
