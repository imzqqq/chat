import * as React from "react";
import { toDataURL, QRCodeSegment, QRCodeToDataURLOptions } from "qrcode";
import classNames from "classnames";

import { _t } from "../../../languageHandler";
import Spinner from "./Spinner";

interface IProps extends QRCodeToDataURLOptions {
    data: string | QRCodeSegment[];
    className?: string;
}

const defaultOptions: QRCodeToDataURLOptions = {
    errorCorrectionLevel: 'L', // we want it as trivial-looking as possible
};

const QRCode: React.FC<IProps> = ({ data, className, ...options }) => {
    const [dataUri, setUri] = React.useState<string>(null);
    React.useEffect(() => {
        let cancelled = false;
        toDataURL(data, { ...defaultOptions, ...options }).then(uri => {
            if (cancelled) return;
            setUri(uri);
        });
        return () => {
            cancelled = true;
        };
    }, [JSON.stringify(data), options]); // eslint-disable-line react-hooks/exhaustive-deps

    return <div className={classNames("mx_QRCode", className)}>
        { dataUri ? <img src={dataUri} className="mx_VerificationQRCode" alt={_t("QR Code")} /> : <Spinner /> }
    </div>;
};

export default QRCode;
