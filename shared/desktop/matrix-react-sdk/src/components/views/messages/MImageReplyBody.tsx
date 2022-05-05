import React from "react";
import MImageBody from "./MImageBody";
import { presentableTextForFile } from "../../../utils/FileUtils";
import { IMediaEventContent } from "../../../customisations/models/IMediaEventContent";
import SenderProfile from "./SenderProfile";
import { EventType } from "matrix-js-sdk/src/@types/event";
import { _t } from "../../../languageHandler";

const FORCED_IMAGE_HEIGHT = 44;

export default class MImageReplyBody extends MImageBody {
    public onClick = (ev: React.MouseEvent): void => {
        ev.preventDefault();
    };

    public wrapImage(contentUrl: string, children: JSX.Chat): JSX.Chat {
        return children;
    }

    // Don't show "Download this_file.png ..."
    public getFileBody(): string {
        const sticker = this.props.mxEvent.getType() === EventType.Sticker;
        return presentableTextForFile(this.props.mxEvent.getContent(), sticker ? _t("Sticker") : _t("Image"), !sticker);
    }

    render() {
        if (this.state.error !== null) {
            return super.render();
        }

        const content = this.props.mxEvent.getContent<IMediaEventContent>();

        const contentUrl = this.getContentUrl();
        const thumbnail = this.messageContent(contentUrl, this.getThumbUrl(), content, FORCED_IMAGE_HEIGHT);
        const fileBody = this.getFileBody();
        const sender = <SenderProfile
            mxEvent={this.props.mxEvent}
            enableFlair={false}
        />;

        return <div className="mx_MImageReplyBody">
            { thumbnail }
            <div className="mx_MImageReplyBody_info">
                <div className="mx_MImageReplyBody_sender">{ sender }</div>
                <div className="mx_MImageReplyBody_filename">{ fileBody }</div>
            </div>
        </div>;
    }
}
