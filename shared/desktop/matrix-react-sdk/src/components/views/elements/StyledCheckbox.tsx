import React from "react";
import { randomString } from "matrix-js-sdk/src/randomstring";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps extends React.InputHTMLAttributes<HTMLInputElement> {
}

interface IState {
}

@replaceableComponent("views.elements.StyledCheckbox")
export default class StyledCheckbox extends React.PureComponent<IProps, IState> {
    private id: string;

    public static readonly defaultProps = {
        className: "",
    };

    constructor(props: IProps) {
        super(props);
        // 56^10 so unlikely chance of collision.
        this.id = "checkbox_" + randomString(10);
    }

    public render() {
        /* eslint @typescript-eslint/no-unused-vars: ["error", { "ignoreRestSiblings": true }] */
        const { children, className, ...otherProps } = this.props;
        return <span className={"mx_Checkbox " + className}>
            <input id={this.id} {...otherProps} type="checkbox" />
            <label htmlFor={this.id}>
                { /* Using the div to center the image */ }
                <div className="mx_Checkbox_background">
                    <img src={require("../../../../res/img/feather-customised/check.svg")} />
                </div>
                <div>
                    { this.props.children }
                </div>
            </label>
        </span>;
    }
}
