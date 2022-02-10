import sanitizeHtml from 'sanitize-html';

export interface IExtendedSanitizeOptions extends sanitizeHtml.IOptions {
    // This option only exists in 2.x RCs so far, so not yet present in the
    // separate type definition module.
    nestingLimit?: number;
}
