import { IPreview } from "./IPreview";
import { TagID } from "../models";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { getSenderName, isSelf, shouldPrefixMessagesIn } from "./utils";
import { _t } from "../../../languageHandler";

export class CallAnswerEventPreview implements IPreview {
    public getTextFor(event: MatrixEvent, tagId?: TagID): string {
        if (shouldPrefixMessagesIn(event.getRoomId(), tagId)) {
            if (isSelf(event)) {
                return _t("You joined the call");
            } else {
                return _t("%(senderName)s joined the call", { senderName: getSenderName(event) });
            }
        } else {
            return _t("Call in progress");
        }
    }
}
