import * as React from "react";
import { ComponentClass } from "../../@types/common";
import NonUrgentToastStore from "../../stores/NonUrgentToastStore";
import { UPDATE_EVENT } from "../../stores/AsyncStore";
import { replaceableComponent } from "../../utils/replaceableComponent";

interface IProps {
}

interface IState {
    toasts: ComponentClass[];
}

@replaceableComponent("structures.NonUrgentToastContainer")
export default class NonUrgentToastContainer extends React.PureComponent<IProps, IState> {
    public constructor(props, context) {
        super(props, context);

        this.state = {
            toasts: NonUrgentToastStore.instance.components,
        };

        NonUrgentToastStore.instance.on(UPDATE_EVENT, this.onUpdateToasts);
    }

    public componentWillUnmount() {
        NonUrgentToastStore.instance.off(UPDATE_EVENT, this.onUpdateToasts);
    }

    private onUpdateToasts = () => {
        this.setState({ toasts: NonUrgentToastStore.instance.components });
    };

    public render() {
        const toasts = this.state.toasts.map((t, i) => {
            return (
                <div className="mx_NonUrgentToastContainer_toast" key={`toast-${i}`}>
                    { React.createElement(t, {}) }
                </div>
            );
        });

        return (
            <div className="mx_NonUrgentToastContainer" role="alert">
                { toasts }
            </div>
        );
    }
}
