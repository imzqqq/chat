/**
 * Interface for classes that actually produce permalinks (strings).
 * TODO: Convert this to a real TypeScript interface
 */
export default class PermalinkConstructor {
    forEvent(roomId: string, eventId: string, serverCandidates: string[] = []): string {
        throw new Error("Not implemented");
    }

    forRoom(roomIdOrAlias: string, serverCandidates: string[] = []): string {
        throw new Error("Not implemented");
    }

    forGroup(groupId: string): string {
        throw new Error("Not implemented");
    }

    forUser(userId: string): string {
        throw new Error("Not implemented");
    }

    forEntity(entityId: string): string {
        throw new Error("Not implemented");
    }

    isPermalinkHost(host: string): boolean {
        throw new Error("Not implemented");
    }

    parsePermalink(fullUrl: string): PermalinkParts {
        throw new Error("Not implemented");
    }
}

// Inspired by/Borrowed with permission from the matrix-bot-sdk:
// https://github.com/turt2live/matrix-js-bot-sdk/blob/7c4665c9a25c2c8e0fe4e509f2616505b5b66a1c/src/Permalinks.ts#L1-L6
export class PermalinkParts {
    roomIdOrAlias: string;
    eventId: string;
    userId: string;
    groupId: string;
    viaServers: string[];

    constructor(roomIdOrAlias: string, eventId: string, userId: string, groupId: string, viaServers: string[]) {
        this.roomIdOrAlias = roomIdOrAlias;
        this.eventId = eventId;
        this.groupId = groupId;
        this.userId = userId;
        this.viaServers = viaServers;
    }

    static forUser(userId: string): PermalinkParts {
        return new PermalinkParts(null, null, userId, null, null);
    }

    static forGroup(groupId: string): PermalinkParts {
        return new PermalinkParts(null, null, null, groupId, null);
    }

    static forRoom(roomIdOrAlias: string, viaServers: string[] = []): PermalinkParts {
        return new PermalinkParts(roomIdOrAlias, null, null, null, viaServers);
    }

    static forEvent(roomId: string, eventId: string, viaServers: string[] = []): PermalinkParts {
        return new PermalinkParts(roomId, eventId, null, null, viaServers);
    }

    get primaryEntityId(): string {
        return this.roomIdOrAlias || this.userId || this.groupId;
    }

    get sigil(): string {
        return this.primaryEntityId[0];
    }
}
