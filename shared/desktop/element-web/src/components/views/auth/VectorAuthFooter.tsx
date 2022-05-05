import React from 'react';
import SdkConfig from 'matrix-react-sdk/src/SdkConfig';
import { _t } from 'matrix-react-sdk/src/languageHandler';

const VectorAuthFooter = () => {
    const brandingConfig = SdkConfig.get().branding;
    let links = [
        { "text": "About", "url": "https://apps.chat.imzqqq.top" },
        { "text": "GitHub", "url": "https://github.com/imzqqq" },
    ];

    if (brandingConfig && brandingConfig.authFooterLinks) {
        links = brandingConfig.authFooterLinks;
    }

    const authFooterLinks = [];
    for (const linkEntry of links) {
        authFooterLinks.push(
            <a href={linkEntry.url} key={linkEntry.text} target="_blank" rel="noreferrer noopener">
                { linkEntry.text }
            </a>,
        );
    }

    return (
        <div className="mx_AuthFooter">
            { authFooterLinks }
            <a href="https://imzqqq.top" target="_blank" rel="noreferrer noopener">{ _t('Powered by Chat') }</a>
        </div>
    );
};

VectorAuthFooter.replaces = 'AuthFooter';

export default VectorAuthFooter;
