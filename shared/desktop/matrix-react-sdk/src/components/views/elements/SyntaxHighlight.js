import React from 'react';
import PropTypes from 'prop-types';
import { highlightBlock } from 'highlight.js';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.elements.SyntaxHighlight")
export default class SyntaxHighlight extends React.Component {
    static propTypes = {
        className: PropTypes.string,
        children: PropTypes.node,
    };

    constructor(props) {
        super(props);

        this._ref = this._ref.bind(this);
    }

    // componentDidUpdate used here for reusability
    componentDidUpdate() {
        if (this._el) highlightBlock(this._el);
    }

    // call componentDidUpdate because _ref is fired on initial render
    // which does not fire componentDidUpdate
    _ref(el) {
        this._el = el;
        this.componentDidUpdate();
    }

    render() {
        const { className, children } = this.props;

        return <pre className={`${className} mx_SyntaxHighlight`} ref={this._ref}>
            <code>{ children }</code>
        </pre>;
    }
}
