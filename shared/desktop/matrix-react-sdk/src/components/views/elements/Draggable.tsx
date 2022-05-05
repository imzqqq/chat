import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    className: string;
    dragFunc: (currentLocation: ILocationState, event: MouseEvent) => ILocationState;
    onMouseUp: (event: MouseEvent) => void;
}

interface IState {
    onMouseMove: (event: MouseEvent) => void;
    onMouseUp: (event: MouseEvent) => void;
    location: ILocationState;
}

export interface ILocationState {
    currentX: number;
    currentY: number;
}

@replaceableComponent("views.elements.Draggable")
export default class Draggable extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            onMouseMove: this.onMouseMove.bind(this),
            onMouseUp: this.onMouseUp.bind(this),
            location: {
                currentX: 0,
                currentY: 0,
            },
        };
    }

    private onMouseDown = (event: MouseEvent): void => {
        this.setState({
            location: {
                currentX: event.clientX,
                currentY: event.clientY,
            },
        });

        document.addEventListener("mousemove", this.state.onMouseMove);
        document.addEventListener("mouseup", this.state.onMouseUp);
    };

    private onMouseUp = (event: MouseEvent): void => {
        document.removeEventListener("mousemove", this.state.onMouseMove);
        document.removeEventListener("mouseup", this.state.onMouseUp);
        this.props.onMouseUp(event);
    };

    private onMouseMove(event: MouseEvent): void {
        const newLocation = this.props.dragFunc(this.state.location, event);

        this.setState({
            location: newLocation,
        });
    }

    render() {
        return <div className={this.props.className} onMouseDown={this.onMouseDown.bind(this)} />;
    }
}
