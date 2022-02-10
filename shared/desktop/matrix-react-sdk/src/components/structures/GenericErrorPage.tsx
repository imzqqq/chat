import React from 'react';
import { replaceableComponent } from "../../utils/replaceableComponent";

interface IProps {
    title: React.ReactNode;
    message: React.ReactNode;
}

@replaceableComponent("structures.GenericErrorPage")
export default class GenericErrorPage extends React.PureComponent<IProps> {
    render() {
        return <div className='mx_GenericErrorPage'>
            <div className='mx_GenericErrorPage_box'>
                <h1>{ this.props.title }</h1>
                <p>{ this.props.message }</p>
            </div>
        </div>;
    }
}
