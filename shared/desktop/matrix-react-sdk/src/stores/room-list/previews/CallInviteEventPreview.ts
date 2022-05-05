import { IPreview } from "./IPreview";
import { TagID } from "../models";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { getSenderName, isSelf, shouldPrefixMessagesIn } from "./utils";
import { _t } from "../../../languageHandler";

export class CallInviteEventPreview implements IPreview {
    public getTextFor(event: MatrixEvent, tagId?: TagID): string {
        if (shouldPrefixMessagesIn(event.getRoomId(), tagId)) {
            if (isSelf(event)) {
                return _t("You started a call");
            } else {
                return _t("%(senderName)s started a call", { senderName: getSenderName(event) });
            }
        } else {
            if (isSelf(event)) {
                return _t("Waiting for answer");
            } else {
                return _t("%(senderName)s is calling", { senderName: getSenderName(event) });
            }
        }
    }
}
