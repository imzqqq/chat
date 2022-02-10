import React from "react";
import SvgSpinner from "./SvgSpinner";

interface IProps {
    w?: number;
    h?: number;
    message?: string;
}

export default class Spinner extends React.PureComponent<IProps> {
    public static defaultProps: Partial<IProps> = {
        w: 32,
        h: 32,
    };

    public render() {
        return (
            <div className="mx_Spinner">
                { this.props.message &&
                    <React.Fragment><div className="mx_Spinner_Msg">{ this.props.message }</div>&nbsp;</React.Fragment>
                }
                <SvgSpinner
                    w={this.props.w}
                    h={this.props.h}
                    className="mx_Spinner_icon"
                />
            </div>
        );
    }
}
