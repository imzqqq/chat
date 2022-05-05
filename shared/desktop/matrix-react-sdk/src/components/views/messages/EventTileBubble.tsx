import React, { forwardRef, ReactNode, ReactChildren } from "react";
import classNames from "classnames";

interface IProps {
    className: string;
    title: string;
    subtitle?: ReactNode;
    children?: ReactChildren;
}

const EventTileBubble = forwardRef<HTMLDivElement, IProps>(({ className, title, subtitle, children }, ref) => {
    return <div className={classNames("mx_EventTileBubble", className)} ref={ref}>
        <div className="mx_EventTileBubble_title">{ title }</div>
        { subtitle && <div className="mx_EventTileBubble_subtitle">{ subtitle }</div> }
        { children }
    </div>;
});

export default EventTileBubble;
