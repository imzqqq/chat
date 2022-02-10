import React from "react";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import SvgSpinner from "./SvgSpinner";

interface IProps {
    w?: number;
    h?: number;
    children?: React.ReactNode;
}

@replaceableComponent("views.elements.InlineSpinner")
export default class InlineSpinner extends React.PureComponent<IProps> {
    static defaultProps = {
        w: 32,
        h: 32,
    };

    render() {
        return (
            <div className="mx_InlineSpinner">
                <SvgSpinner
                    w={this.props.w}
                    h={this.props.h}
                    className="mx_InlineSpinner_icon mx_Spinner_icon"
                />
            </div>
        );
    }
}
