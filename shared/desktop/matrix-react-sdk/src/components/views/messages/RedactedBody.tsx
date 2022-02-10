import React, { useContext } from "react";
import { MatrixClient } from "matrix-js-sdk/src/client";
import { _t } from "../../../languageHandler";
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { formatFullDate } from "../../../DateUtils";
import SettingsStore from "../../../settings/SettingsStore";
import { IBodyProps } from "./IBodyProps";

const RedactedBody = React.forwardRef<any, IBodyProps>(({ mxEvent }, ref) => {
    const cli: MatrixClient = useContext(MatrixClientContext);

    let text = _t("Message deleted");
    const unsigned = mxEvent.getUnsigned();
    const redactedBecauseUserId = unsigned && unsigned.redacted_because && unsigned.redacted_because.sender;
    if (redactedBecauseUserId && redactedBecauseUserId !== mxEvent.getSender()) {
        const room = cli.getRoom(mxEvent.getRoomId());
        const sender = room && room.getMember(redactedBecauseUserId);
        text = _t("Message deleted by %(name)s", { name: sender ? sender.name : redactedBecauseUserId });
    }

    const showTwelveHour = SettingsStore.getValue("showTwelveHourTimestamps");
    const fullDate = formatFullDate(new Date(unsigned.redacted_because.origin_server_ts), showTwelveHour);
    const titleText = _t("Message deleted on %(date)s", { date: fullDate });

    return (
        <span className="mx_RedactedBody" ref={ref} title={titleText}>
            { text }
        </span>
    );
});

export default RedactedBody;
