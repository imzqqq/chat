import React from 'react';
import CallPreview from './CallPreview';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {

}

interface IState {

}

@replaceableComponent("views.voip.CallContainer")
export default class CallContainer extends React.PureComponent<IProps, IState> {
    public render() {
        return <div className="mx_CallContainer">
            <CallPreview />
        </div>;
    }
}
