import React from 'react';
import classnames from 'classnames';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps extends React.InputHTMLAttributes<HTMLInputElement> {
    outlined?: boolean;
    // If true (default), the children will be contained within a <label> element
    // If false, they'll be in a div. Putting interactive components that have labels
    // themselves in labels can cause strange bugs like https://github.com/vector-im/element-web/issues/18031
    childrenInLabel?: boolean;
}

interface IState {
}

@replaceableComponent("views.elements.StyledRadioButton")
export default class StyledRadioButton extends React.PureComponent<IProps, IState> {
    public static readonly defaultProps = {
        className: '',
        childrenInLabel: true,
    };

    public render() {
        const { children, className, disabled, outlined, childrenInLabel, ...otherProps } = this.props;
        const _className = classnames(
            'mx_RadioButton',
            className,
            {
                "mx_RadioButton_disabled": disabled,
                "mx_RadioButton_enabled": !disabled,
                "mx_RadioButton_checked": this.props.checked,
                "mx_RadioButton_outlined": outlined,
            });

        const radioButton = <React.Fragment>
            <input type='radio' disabled={disabled} {...otherProps} />
            { /* Used to render the radio button circle */ }
            <div><div /></div>
        </React.Fragment>;

        if (childrenInLabel) {
            return <label className={_className}>
                { radioButton }
                <div className="mx_RadioButton_content">{ children }</div>
                <div className="mx_RadioButton_spacer" />
            </label>;
        } else {
            return <div className={_className}>
                <label className="mx_RadioButton_innerLabel">
                    { radioButton }
                </label>
                <div className="mx_RadioButton_content">{ children }</div>
                <div className="mx_RadioButton_spacer" />
            </div>;
        }
    }
}
