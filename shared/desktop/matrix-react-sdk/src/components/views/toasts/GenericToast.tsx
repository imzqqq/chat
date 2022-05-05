import React, { ReactNode } from "react";
import AccessibleButton from "../elements/AccessibleButton";

import { XOR } from "../../../@types/common";

export interface IProps {
    description: ReactNode;
    detail?: ReactNode;
    acceptLabel: string;

    onAccept();
}

interface IPropsExtended extends IProps {
    rejectLabel: string;
    onReject();
}

const GenericToast: React.FC<XOR<IPropsExtended, IProps>> = ({
    description,
    detail,
    acceptLabel,
    rejectLabel,
    onAccept,
    onReject,
}) => {
    const detailContent = detail ? <div className="mx_Toast_detail">
        { detail }
    </div> : null;

    return <div>
        <div className="mx_Toast_description">
            { description }
            { detailContent }
        </div>
        <div className="mx_Toast_buttons" aria-live="off">
            { onReject && rejectLabel && <AccessibleButton kind="danger_outline" onClick={onReject}>
                { rejectLabel }
            </AccessibleButton> }
            <AccessibleButton onClick={onAccept} kind="primary">
                { acceptLabel }
            </AccessibleButton>
        </div>
    </div>;
};

export default GenericToast;
