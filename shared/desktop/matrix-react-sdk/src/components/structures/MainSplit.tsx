import React from 'react';
import { NumberSize, Resizable } from 're-resizable';
import { replaceableComponent } from "../../utils/replaceableComponent";
import ResizeNotifier from "../../utils/ResizeNotifier";
import { Direction } from "re-resizable/lib/resizer";

interface IProps {
    resizeNotifier: ResizeNotifier;
    collapsedRhs?: boolean;
    panel?: JSX.Chat;
}

@replaceableComponent("structures.MainSplit")
export default class MainSplit extends React.Component<IProps> {
    private onResizeStart = (): void => {
        this.props.resizeNotifier.startResizing();
    };

    private onResize = (): void => {
        this.props.resizeNotifier.notifyRightHandleResized();
    };

    private onResizeStop = (
        event: MouseEvent | TouchEvent, direction: Direction, elementRef: HTMLElement, delta: NumberSize,
    ): void => {
        this.props.resizeNotifier.stopResizing();
        window.localStorage.setItem("mx_rhs_size", (this.loadSidePanelSize().width + delta.width).toString());
    };

    private loadSidePanelSize(): {height: string | number, width: number} {
        let rhsSize = parseInt(window.localStorage.getItem("mx_rhs_size"), 10);

        if (isNaN(rhsSize)) {
            rhsSize = 350;
        }

        return {
            height: "100%",
            width: rhsSize,
        };
    }

    public render(): JSX.Chat {
        const bodyView = React.Children.only(this.props.children);
        const panelView = this.props.panel;

        const hasResizer = !this.props.collapsedRhs && panelView;

        let children;
        if (hasResizer) {
            children = <Resizable
                defaultSize={this.loadSidePanelSize()}
                minWidth={264}
                maxWidth="50%"
                enable={{
                    top: false,
                    right: false,
                    bottom: false,
                    left: true,
                    topRight: false,
                    bottomRight: false,
                    bottomLeft: false,
                    topLeft: false,
                }}
                onResizeStart={this.onResizeStart}
                onResize={this.onResize}
                onResizeStop={this.onResizeStop}
                className="mx_RightPanel_ResizeWrapper"
                handleClasses={{ left: "mx_RightPanel_ResizeHandle" }}
            >
                { panelView }
            </Resizable>;
        }

        return <div className="mx_MainSplit">
            { bodyView }
            { children }
        </div>;
    }
}
