import React, { HTMLAttributes, WheelEvent } from "react";

interface IProps extends Omit<HTMLAttributes<HTMLDivElement>, "onScroll"> {
    className?: string;
    onScroll?: (event: Event) => void;
    onWheel?: (event: WheelEvent) => void;
    style?: React.CSSProperties;
    tabIndex?: number;
    wrappedRef?: (ref: HTMLDivElement) => void;
}

export default class AutoHideScrollbar extends React.Component<IProps> {
    private containerRef: React.RefObject<HTMLDivElement> = React.createRef();

    public componentDidMount() {
        if (this.containerRef.current && this.props.onScroll) {
            // Using the passive option to not block the main thread
            // https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener#improving_scrolling_performance_with_passive_listeners
            this.containerRef.current.addEventListener("scroll", this.props.onScroll, { passive: true });
        }

        if (this.props.wrappedRef) {
            this.props.wrappedRef(this.containerRef.current);
        }
    }

    public componentWillUnmount() {
        if (this.containerRef.current && this.props.onScroll) {
            this.containerRef.current.removeEventListener("scroll", this.props.onScroll);
        }
    }

    public getScrollTop(): number {
        return this.containerRef.current.scrollTop;
    }

    public render() {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { className, onScroll, onWheel, style, tabIndex, wrappedRef, children, ...otherProps } = this.props;

        return (<div
            {...otherProps}
            ref={this.containerRef}
            style={style}
            className={["mx_AutoHideScrollbar", className].join(" ")}
            onWheel={onWheel}
            // Firefox sometimes makes this element focusable due to
            // overflow:scroll;, so force it out of tab order by default.
            tabIndex={tabIndex ?? -1}
        >
            { children }
        </div>);
    }
}
