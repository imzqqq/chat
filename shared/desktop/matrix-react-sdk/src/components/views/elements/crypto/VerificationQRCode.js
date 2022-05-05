import React from "react";
import PropTypes from "prop-types";
import { replaceableComponent } from "../../../../utils/replaceableComponent";
import QRCode from "../QRCode";

@replaceableComponent("views.elements.crypto.VerificationQRCode")
export default class VerificationQRCode extends React.PureComponent {
    static propTypes = {
        qrCodeData: PropTypes.object.isRequired,
    };

    render() {
        return (
            <QRCode
                data={[{ data: this.props.qrCodeData.buffer, mode: 'byte' }]}
                className="mx_VerificationQRCode"
                width={196} />
        );
    }
}
