import { isEnumValue } from "../../utils/enums";

// TODO?
export enum DefaultTagID {
    Invite = "im.vector.fake.invite",
    Untagged = "im.vector.fake.recent", // legacy: used to just be 'recent rooms' but now it's all untagged rooms
    Archived = "im.vector.fake.archived",
    LowPriority = "m.lowpriority",
    Favourite = "m.favourite",
    DM = "im.vector.fake.direct",
    Unified = "de.spiritcroc.fake.unified",
    ServerNotice = "m.server_notice",
    Suggested = "im.vector.fake.suggested",
}

export const OrderedDefaultTagIDs = [
    DefaultTagID.Invite,
    DefaultTagID.Favourite,
    DefaultTagID.Unified,
    DefaultTagID.DM,
    DefaultTagID.Untagged,
    DefaultTagID.LowPriority,
    DefaultTagID.ServerNotice,
    DefaultTagID.Suggested,
    DefaultTagID.Archived,
];

export type TagID = string | DefaultTagID;

export function isCustomTag(tagId: TagID): boolean {
    return !isEnumValue(DefaultTagID, tagId);
}

export enum RoomUpdateCause {
    Timeline = "TIMELINE",
    PossibleTagChange = "POSSIBLE_TAG_CHANGE",
    ReadReceipt = "READ_RECEIPT",
    NewRoom = "NEW_ROOM",
    RoomRemoved = "ROOM_REMOVED",
}
