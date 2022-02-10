export interface IImageInfo {
    size?: number;
    mimetype?: string;
    thumbnail_info?: { // eslint-disable-line camelcase
        w?: number;
        h?: number;
        size?: number;
        mimetype?: string;
    };
    w?: number;
    h?: number;
}

export enum Visibility {
    Public = "public",
    Private = "private",
}

export enum Preset {
    PrivateChat = "private_chat",
    TrustedPrivateChat = "trusted_private_chat",
    PublicChat = "public_chat",
}

export type ResizeMethod = "crop" | "scale";

// TODO move to http-api after TSification
export interface IAbortablePromise<T> extends Promise<T> {
    abort(): void;
}

export type IdServerUnbindResult = "no-support" | "success";

// Knock and private are reserved keywords which are not yet implemented.
export enum JoinRule {
    Public = "public",
    Invite = "invite",
    /**
     * @deprecated Reserved keyword. Should not be used. Not yet implemented.
     */
    Private = "private",
    Knock = "knock",
    Restricted = "restricted",
}

export enum RestrictedAllowType {
    RoomMembership = "m.room_membership",
}

export interface IJoinRuleEventContent {
    join_rule: JoinRule; // eslint-disable-line camelcase
    allow?: {
        type: RestrictedAllowType;
        room_id: string; // eslint-disable-line camelcase
    }[];
}

export enum GuestAccess {
    CanJoin = "can_join",
    Forbidden = "forbidden",
}

export enum HistoryVisibility {
    Invited = "invited",
    Joined = "joined",
    Shared = "shared",
    WorldReadable = "world_readable",
}
