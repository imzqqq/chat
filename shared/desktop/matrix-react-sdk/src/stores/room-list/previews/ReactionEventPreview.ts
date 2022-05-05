import { IPreview } from "./IPreview";
import { TagID } from "../models";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { getSenderName, isSelf, shouldPrefixMessagesIn } from "./utils";
import { _t } from "../../../languageHandler";
import SettingsStore from "../../../settings/SettingsStore";
import DMRoomMap from "../../../utils/DMRoomMap";

export class ReactionEventPreview implements IPreview {
    public getTextFor(event: MatrixEvent, tagId?: TagID): string {
        const showDms = SettingsStore.getValue("feature_roomlist_preview_reactions_dms");
        const showAll = SettingsStore.getValue("feature_roomlist_preview_reactions_all");

        // If we're not showing all reactions, see if we're showing DMs instead
        if (!showAll) {
            // If we're not showing reactions on DMs, or we are and the room isn't a DM, skip
            if (!(showDms && DMRoomMap.shared().getUserIdForRoomId(event.getRoomId()))) {
                return null;
            }
        }

        const relation = event.getRelation();
        if (!relation) return null; // invalid reaction (probably redacted)

        const reaction = relation.key;
        if (!reaction) return null; // invalid reaction (unknown format)

        if (isSelf(event) || !shouldPrefixMessagesIn(event.getRoomId(), tagId)) {
            return reaction;
        } else {
            return _t("%(senderName)s: %(reaction)s", { senderName: getSenderName(event), reaction });
        }
    }
}
