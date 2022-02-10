import React from "react";
import { _t } from "../../../languageHandler";

interface IProps {
    w?: number;
    h?: number;
    className?: string;
}

export default class InlineSpinner extends React.PureComponent<IProps> {
    static defaultProps = {
        w: 32,
        h: 32,
        className: "mx_Spinner_icon",
    };

    render() {
        return (
            // loading-bubbles.svg from https://github.com/jxnblk/loading
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 32 32"
                fill="currentColor"
                width={this.props.w}
                height={this.props.h}
                className={this.props.className}
                aria-label={_t("Loading...")}
            >
                <circle transform="translate(8 0)" cx="0" cy="16" r="0">
                    <animate
                        attributeName="r"
                        values="0; 4; 0; 0"
                        dur="1.2s"
                        repeatCount="indefinite"
                        begin="0"
                        keyTimes="0;0.2;0.7;1"
                        keySplines="0.2 0.2 0.4 0.8;0.2 0.6 0.4 0.8;0.2 0.6 0.4 0.8"
                        calcMode="spline"
                    />
                </circle>
                <circle transform="translate(16 0)" cx="0" cy="16" r="0">
                    <animate
                        attributeName="r"
                        values="0; 4; 0; 0"
                        dur="1.2s"
                        repeatCount="indefinite"
                        begin="0.3"
                        keyTimes="0;0.2;0.7;1"
                        keySplines="0.2 0.2 0.4 0.8;0.2 0.6 0.4 0.8;0.2 0.6 0.4 0.8"
                        calcMode="spline"
                    />
                </circle>
                <circle transform="translate(24 0)" cx="0" cy="16" r="0">
                    <animate
                        attributeName="r"
                        values="0; 4; 0; 0"
                        dur="1.2s"
                        repeatCount="indefinite"
                        begin="0.6"
                        keyTimes="0;0.2;0.7;1"
                        keySplines="0.2 0.2 0.4 0.8;0.2 0.6 0.4 0.8;0.2 0.6 0.4 0.8"
                        calcMode="spline"
                    />
                </circle>
            </svg>
        );
    }
}
