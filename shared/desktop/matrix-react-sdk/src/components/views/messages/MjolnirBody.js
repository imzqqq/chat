import React from 'react';
import PropTypes from 'prop-types';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.messages.MjolnirBody")
export default class MjolnirBody extends React.Component {
    static propTypes = {
        mxEvent: PropTypes.object.isRequired,
        onMessageAllowed: PropTypes.func.isRequired,
    };

    constructor() {
        super();
    }

    _onAllowClick = (e) => {
        e.preventDefault();
        e.stopPropagation();

        const key = `mx_mjolnir_render_${this.props.mxEvent.getRoomId()}__${this.props.mxEvent.getId()}`;
        localStorage.setItem(key, "true");
        this.props.onMessageAllowed();
    };

    render() {
        return (
            <div className='mx_MjolnirBody'><i>{ _t(
                "You have ignored this user, so their message is hidden. <a>Show anyways.</a>",
                {}, { a: (sub) => <a href="#" onClick={this._onAllowClick}>{ sub }</a> },
            ) }</i></div>
        );
    }
}
