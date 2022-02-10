import { MatrixEvent } from "matrix-js-sdk/src";
import { TileShape } from "../rooms/EventTile";
import { MediaEventHelper } from "../../../utils/MediaEventHelper";
import EditorStateTransfer from "../../../utils/EditorStateTransfer";
import { RoomPermalinkCreator } from "../../../utils/permalinks/Permalinks";
import { Layout } from "../../../settings/Layout";

export interface IBodyProps {
    mxEvent: MatrixEvent;

    /* a list of words to highlight */
    highlights: string[];

    /* link URL for the highlights */
    highlightLink: string;

    /* callback called when dynamic content in events are loaded */
    onHeightChanged: () => void;

    showUrlPreview?: boolean;
    tileShape: TileShape;
    maxImageHeight?: number;
    replacingEventId?: string;
    editState?: EditorStateTransfer;
    onMessageAllowed: () => void; // TODO: Docs
    permalinkCreator: RoomPermalinkCreator;
    mediaEventHelper: MediaEventHelper;
    layout: Layout;
    scBubble: boolean;
    scBubbleGroupTimestamp: any;
    scBubbleActionBar: any;
}
