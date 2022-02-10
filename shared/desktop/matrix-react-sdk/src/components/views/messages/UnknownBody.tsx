import React, { forwardRef } from "react";
import { MatrixEvent } from "matrix-js-sdk/src";

interface IProps {
    mxEvent: MatrixEvent;
    children?: React.ReactNode;
}

export default forwardRef(({ mxEvent, children }: IProps, ref: React.RefObject<HTMLSpanElement>) => {
    const text = mxEvent.getContent().body;
    return (
        <span className="mx_UnknownBody" ref={ref}>
            { text }
            { children }
        </span>
    );
});
