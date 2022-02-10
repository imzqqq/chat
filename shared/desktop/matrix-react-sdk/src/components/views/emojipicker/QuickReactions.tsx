import React from 'react';

import { _t } from '../../../languageHandler';
import { getEmojiFromUnicode, IEmoji } from "../../../emoji";
import Emoji from "./Emoji";
import { replaceableComponent } from "../../../utils/replaceableComponent";

// We use the variation-selector Heart in Quick Reactions for some reason
const QUICK_REACTIONS = ["👍", "👎", "😄", "🎉", "😕", "❤️", "🚀", "👀"].map(emoji => {
    const data = getEmojiFromUnicode(emoji);
    if (!data) {
        throw new Error(`Emoji ${emoji} doesn't exist in emojibase`);
    }
    return data;
});

interface IProps {
    selectedEmojis?: Set<string>;
    onClick(emoji: IEmoji): void;
}

interface IState {
    hover?: IEmoji;
}

@replaceableComponent("views.emojipicker.QuickReactions")
class QuickReactions extends React.Component<IProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            hover: null,
        };
    }

    private onMouseEnter = (emoji: IEmoji) => {
        this.setState({
            hover: emoji,
        });
    };

    private onMouseLeave = () => {
        this.setState({
            hover: null,
        });
    };

    render() {
        return (
            <section className="mx_EmojiPicker_footer mx_EmojiPicker_quick mx_EmojiPicker_category">
                <h2 className="mx_EmojiPicker_quick_header mx_EmojiPicker_category_label">
                    { !this.state.hover
                        ? _t("Quick Reactions")
                        : <React.Fragment>
                            <span className="mx_EmojiPicker_name">{ this.state.hover.annotation }</span>
                            <span className="mx_EmojiPicker_shortcode">{ this.state.hover.shortcodes[0] }</span>
                        </React.Fragment>
                    }
                </h2>
                <ul className="mx_EmojiPicker_list" aria-label={_t("Quick Reactions")}>
                    { QUICK_REACTIONS.map(emoji => ((
                        <Emoji
                            key={emoji.hexcode}
                            emoji={emoji}
                            onClick={this.props.onClick}
                            onMouseEnter={this.onMouseEnter}
                            onMouseLeave={this.onMouseLeave}
                            selectedEmojis={this.props.selectedEmojis}
                        />
                    ))) }
                </ul>
            </section>
        );
    }
}

export default QuickReactions;
