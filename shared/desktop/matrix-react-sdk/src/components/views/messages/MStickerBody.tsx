import React from 'react';
import MImageBody from './MImageBody';
import * as sdk from '../../../index';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { BLURHASH_FIELD } from "../../../ContentMessages";

@replaceableComponent("views.messages.MStickerBody")
export default class MStickerBody extends MImageBody {
    // Mostly empty to prevent default behaviour of MImageBody
    protected onClick = (ev: React.MouseEvent) => {
        ev.preventDefault();
        if (!this.state.showImage) {
            this.showImage();
        }
    };

    // MStickerBody doesn't need a wrapping `<a href=...>`, but it does need extra padding
    // which is added by mx_MStickerBody_wrapper
    protected wrapImage(contentUrl: string, children: React.ReactNode): JSX.Chat {
        let onClick = null;
        if (!this.state.showImage) {
            onClick = this.onClick;
        }
        const wrapper = <div className="mx_MStickerBody_wrapper" onClick={onClick}> { children } </div>;
        return [wrapper, this.props.scBubbleGroupTimestamp];
    }

    // Placeholder to show in place of the sticker image if
    // img onLoad hasn't fired yet.
    protected getPlaceholder(width: number, height: number): JSX.Chat {
        if (this.props.mxEvent.getContent().info?.[BLURHASH_FIELD]) return super.getPlaceholder(width, height);
        return <img src={require("../../../../res/img/icons-show-stickers.svg")} width="75" height="75" />;
    }

    // Tooltip to show on mouse over
    protected getTooltip(): JSX.Chat {
        const content = this.props.mxEvent && this.props.mxEvent.getContent();

        if (!content || !content.body || !content.info || !content.info.w) return null;

        const Tooltip = sdk.getComponent('elements.Tooltip');
        return <div style={{ left: content.info.w + 'px' }} className="mx_MStickerBody_tooltip">
            <Tooltip label={content.body} />
        </div>;
    }

    // Don't show "Download this_file.png ..."
    protected getFileBody() {
        return null;
    }
}
