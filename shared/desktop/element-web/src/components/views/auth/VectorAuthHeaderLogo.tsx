import React from 'react';
import SdkConfig from 'matrix-react-sdk/src/SdkConfig';

export default class VectorAuthHeaderLogo extends React.PureComponent {
    static replaces = 'AuthHeaderLogo';

    render() {
        const brandingConfig = SdkConfig.get().branding;
        let logoUrl = "themes/element/img/logos/element-logo.svg";
        if (brandingConfig && brandingConfig.authHeaderLogoUrl) {
            logoUrl = brandingConfig.authHeaderLogoUrl;
        }

        return (
            <div className="mx_AuthHeaderLogo">
                <img src={logoUrl} alt="Chat" />
            </div>
        );
    }
}
