import React from "react";
import MAudioBody from "./MAudioBody";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import MVoiceMessageBody from "./MVoiceMessageBody";
import { IBodyProps } from "./IBodyProps";
import { isVoiceMessage } from "../../../utils/EventUtils";

@replaceableComponent("views.messages.MVoiceOrAudioBody")
export default class MVoiceOrAudioBody extends React.PureComponent<IBodyProps> {
    public render() {
        if (isVoiceMessage(this.props.mxEvent)) {
            return <MVoiceMessageBody {...this.props} />;
        } else {
            return <MAudioBody {...this.props} />;
        }
    }
}
