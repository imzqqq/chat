import * as React from "react";
import { _t } from "matrix-react-sdk/src/languageHandler";

// directly import the style here as this layer does not support rethemedex at this time so no matrix-react-sdk
// scss variables will be accessible.
import "../../../res/css/structures/ErrorView.scss";

interface IProps {
    // both of these should already be internationalised
    title: string;
    messages?: string[];
}

const ErrorView: React.FC<IProps> = ({ title, messages }) => {
    return <div className="mx_ErrorView">
        <div className="mx_ErrorView_container">
            <div className="mx_HomePage_header">
                <span className="mx_HomePage_logo">
                    <img height="42" src="themes/element/img/logos/element-logo.svg" alt="Chat" />
                </span>
                <h1>{ _t("Failed to start") }</h1>
            </div>
            <div className="mx_HomePage_col">
                <div className="mx_HomePage_row">
                    <div>
                        <h2 id="step1_heading">{ title }</h2>
                        { messages && messages.map(msg => <p key={msg}>
                            { msg }
                        </p>) }
                    </div>
                </div>
            </div>
            <div className="mx_HomePage_row mx_Center mx_Spacer">
                <p className="mx_Spacer">
                    <a href="https://apps.chat.dingshunyu.top" target="_blank" className="mx_FooterLink">
                        Go to apps.chat.dingshunyu.top
                    </a>
                </p>
            </div>
        </div>
    </div>;
};

export default ErrorView;

