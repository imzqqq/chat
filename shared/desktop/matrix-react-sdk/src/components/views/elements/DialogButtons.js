import React from "react";
import PropTypes from "prop-types";
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

/**
 * Basic container for buttons in modal dialogs.
 */
@replaceableComponent("views.elements.DialogButtons")
export default class DialogButtons extends React.Component {
    static propTypes = {
        // The primary button which is styled differently and has default focus.
        primaryButton: PropTypes.node.isRequired,

        // A node to insert into the cancel button instead of default "Cancel"
        cancelButton: PropTypes.node,

        // If true, make the primary button a form submit button (input type="submit")
        primaryIsSubmit: PropTypes.bool,

        // onClick handler for the primary button.
        onPrimaryButtonClick: PropTypes.func,

        // should there be a cancel button? default: true
        hasCancel: PropTypes.bool,

        // The class of the cancel button, only used if a cancel button is
        // enabled
        cancelButtonClass: PropTypes.node,

        // onClick handler for the cancel button.
        onCancel: PropTypes.func,

        focus: PropTypes.bool,

        // disables the primary and cancel buttons
        disabled: PropTypes.bool,

        // disables only the primary button
        primaryDisabled: PropTypes.bool,

        // something to stick next to the buttons, optionally
        additive: PropTypes.element,
    };

    static defaultProps = {
        hasCancel: true,
        disabled: false,
    };

    _onCancelClick = () => {
        this.props.onCancel();
    };

    render() {
        let primaryButtonClassName = "mx_Dialog_primary";
        if (this.props.primaryButtonClass) {
            primaryButtonClassName += " " + this.props.primaryButtonClass;
        }
        let cancelButton;

        if (this.props.cancelButton || this.props.hasCancel) {
            cancelButton = <button
                // important: the default type is 'submit' and this button comes before the
                // primary in the DOM so will get form submissions unless we make it not a submit.
                type="button"
                onClick={this._onCancelClick}
                className={this.props.cancelButtonClass}
                disabled={this.props.disabled}
            >
                { this.props.cancelButton || _t("Cancel") }
            </button>;
        }

        let additive = null;
        if (this.props.additive) {
            additive = <div className="mx_Dialog_buttons_additive">{ this.props.additive }</div>;
        }

        return (
            <div className="mx_Dialog_buttons">
                { additive }
                { cancelButton }
                { this.props.children }
                <button type={this.props.primaryIsSubmit ? 'submit' : 'button'}
                    className={primaryButtonClassName}
                    onClick={this.props.onPrimaryButtonClick}
                    autoFocus={this.props.focus}
                    disabled={this.props.disabled || this.props.primaryDisabled}
                >
                    { this.props.primaryButton }
                </button>
            </div>
        );
    }
}
