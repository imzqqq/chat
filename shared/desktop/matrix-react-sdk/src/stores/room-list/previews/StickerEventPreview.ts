import { IPreview } from "./IPreview";
import { TagID } from "../models";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { getSenderName, isSelf, shouldPrefixMessagesIn } from "./utils";
import { _t } from "../../../languageHandler";

export class StickerEventPreview implements IPreview {
    public getTextFor(event: MatrixEvent, tagId?: TagID): string {
        const stickerName = event.getContent()['body'];
        if (!stickerName) return null;

        if (isSelf(event) || !shouldPrefixMessagesIn(event.getRoomId(), tagId)) {
            return stickerName;
        } else {
            return _t("%(senderName)s: %(stickerName)s", { senderName: getSenderName(event), stickerName });
        }
    }
}
