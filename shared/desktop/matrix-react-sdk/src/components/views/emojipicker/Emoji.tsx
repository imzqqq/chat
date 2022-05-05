import React from 'react';

import { MenuItem } from "../../structures/ContextMenu";
import { IEmoji } from "../../../emoji";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    emoji: IEmoji;
    selectedEmojis?: Set<string>;
    onClick(emoji: IEmoji): void;
    onMouseEnter(emoji: IEmoji): void;
    onMouseLeave(emoji: IEmoji): void;
}

@replaceableComponent("views.emojipicker.Emoji")
class Emoji extends React.PureComponent<IProps> {
    render() {
        const { onClick, onMouseEnter, onMouseLeave, emoji, selectedEmojis } = this.props;
        const isSelected = selectedEmojis && selectedEmojis.has(emoji.unicode);
        return (
            <MenuItem
                element="li"
                onClick={() => onClick(emoji)}
                onMouseEnter={() => onMouseEnter(emoji)}
                onMouseLeave={() => onMouseLeave(emoji)}
                className="mx_EmojiPicker_item_wrapper"
                label={emoji.unicode}
            >
                <div className={`mx_EmojiPicker_item ${isSelected ? 'mx_EmojiPicker_item_selected' : ''}`}>
                    { emoji.unicode }
                </div>
            </MenuItem>
        );
    }
}

export default Emoji;
