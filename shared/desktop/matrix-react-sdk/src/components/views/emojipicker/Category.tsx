import React, { RefObject } from 'react';

import { CATEGORY_HEADER_HEIGHT, EMOJI_HEIGHT, EMOJIS_PER_ROW } from "./EmojiPicker";
import LazyRenderList from "../elements/LazyRenderList";
import { DATA_BY_CATEGORY, IEmoji } from "../../../emoji";
import Emoji from './Emoji';
import { replaceableComponent } from "../../../utils/replaceableComponent";

const OVERFLOW_ROWS = 3;

export type CategoryKey = (keyof typeof DATA_BY_CATEGORY) | "recent";

export interface ICategory {
    id: CategoryKey;
    name: string;
    enabled: boolean;
    visible: boolean;
    ref: RefObject<HTMLButtonElement>;
}

interface IProps {
    id: string;
    name: string;
    emojis: IEmoji[];
    selectedEmojis: Set<string>;
    heightBefore: number;
    viewportHeight: number;
    scrollTop: number;
    onClick(emoji: IEmoji): void;
    onMouseEnter(emoji: IEmoji): void;
    onMouseLeave(emoji: IEmoji): void;
}

@replaceableComponent("views.emojipicker.Category")
class Category extends React.PureComponent<IProps> {
    private renderEmojiRow = (rowIndex: number) => {
        const { onClick, onMouseEnter, onMouseLeave, selectedEmojis, emojis } = this.props;
        const emojisForRow = emojis.slice(rowIndex * 8, (rowIndex + 1) * 8);
        return (<div key={rowIndex}>{
            emojisForRow.map(emoji => ((
                <Emoji
                    key={emoji.hexcode}
                    emoji={emoji}
                    selectedEmojis={selectedEmojis}
                    onClick={onClick}
                    onMouseEnter={onMouseEnter}
                    onMouseLeave={onMouseLeave}
                />
            )))
        }</div>);
    };

    render() {
        const { emojis, name, heightBefore, viewportHeight, scrollTop } = this.props;
        if (!emojis || emojis.length === 0) {
            return null;
        }
        const rows = new Array(Math.ceil(emojis.length / EMOJIS_PER_ROW));
        for (let counter = 0; counter < rows.length; ++counter) {
            rows[counter] = counter;
        }

        const viewportTop = scrollTop;
        const viewportBottom = viewportTop + viewportHeight;
        const listTop = heightBefore + CATEGORY_HEADER_HEIGHT;
        const listBottom = listTop + (rows.length * EMOJI_HEIGHT);
        const top = Math.max(viewportTop, listTop);
        const bottom = Math.min(viewportBottom, listBottom);
        // the viewport height and scrollTop passed to the LazyRenderList
        // is capped at the intersection with the real viewport, so lists
        // out of view are passed height 0, so they won't render any items.
        const localHeight = Math.max(0, bottom - top);
        const localScrollTop = Math.max(0, scrollTop - listTop);

        return (
            <section
                id={`mx_EmojiPicker_category_${this.props.id}`}
                className="mx_EmojiPicker_category"
                data-category-id={this.props.id}
                role="tabpanel"
                aria-label={name}
            >
                <h2 className="mx_EmojiPicker_category_label">
                    { name }
                </h2>
                <LazyRenderList
                    element="ul"
                    className="mx_EmojiPicker_list"
                    itemHeight={EMOJI_HEIGHT}
                    items={rows}
                    scrollTop={localScrollTop}
                    height={localHeight}
                    overflowItems={OVERFLOW_ROWS}
                    overflowMargin={0}
                    renderItem={this.renderEmojiRow}
                />
            </section>
        );
    }
}

export default Category;
