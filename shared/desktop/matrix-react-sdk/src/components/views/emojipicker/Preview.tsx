import React from 'react';

import { IEmoji } from "../../../emoji";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    emoji: IEmoji;
}

@replaceableComponent("views.emojipicker.Preview")
class Preview extends React.PureComponent<IProps> {
    render() {
        const { unicode, annotation, shortcodes: [shortcode] } = this.props.emoji;

        return (
            <div className="mx_EmojiPicker_footer mx_EmojiPicker_preview">
                <div className="mx_EmojiPicker_preview_emoji">
                    { unicode }
                </div>
                <div className="mx_EmojiPicker_preview_text">
                    <div className="mx_EmojiPicker_name mx_EmojiPicker_preview_name">
                        { annotation }
                    </div>
                    <div className="mx_EmojiPicker_shortcode">
                        { shortcode }
                    </div>
                </div>
            </div>
        );
    }
}

export default Preview;
