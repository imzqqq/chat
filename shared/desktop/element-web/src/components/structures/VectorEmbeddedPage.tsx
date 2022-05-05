import EmbeddedPage from 'matrix-react-sdk/src/components/structures/EmbeddedPage';
import sanitizeHtml from 'sanitize-html';
import { _t } from 'matrix-react-sdk/src/languageHandler';

export default class VectorEmbeddedPage extends EmbeddedPage {
    static replaces = 'EmbeddedPage';

    // we're overriding the base component here, for Chat-specific tweaks
    translate(s: string) {
        s = sanitizeHtml(_t(s));
        // ugly fix for https://github.com/vector-im/element-web/issues/4243
        // eslint-disable-next-line max-len
        s = s.replace(/\[matrix\]/, '<a href="https://chat.imzqqq.top" target="_blank" rel="noreferrer noopener"><img width="79" height="34" alt="Chat" style="padding-left: 1px;vertical-align: middle" src="welcome/images/matrix.svg"/></a>');
        return s;
    }
}
