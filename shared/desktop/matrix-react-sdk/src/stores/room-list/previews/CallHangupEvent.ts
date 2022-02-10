import { IPreview } from "./IPreview";
import { TagID } from "../models";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { getSenderName, isSelf, shouldPrefixMessagesIn } from "./utils";
import { _t } from "../../../languageHandler";

export class CallHangupEvent implements IPreview {
    public getTextFor(event: MatrixEvent, tagId?: TagID): string {
        if (shouldPrefixMessagesIn(event.getRoomId(), tagId)) {
            if (isSelf(event)) {
                return _t("You ended the call");
            } else {
                return _t("%(senderName)s ended the call", { senderName: getSenderName(event) });
            }
        } else {
            return _t("Call ended");
        }
    }
}
