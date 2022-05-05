// Pull in the encryption lib so that we can decrypt attachments.
import encrypt from 'browser-encrypt-attachment';
import { mediaFromContent } from "../customisations/Media";
import { IEncryptedFile, IMediaEventInfo } from "../customisations/models/IMediaEventContent";
import { getBlobSafeMimeType } from "./blobs";

/**
 * Decrypt a file attached to a matrix event.
 * @param {IEncryptedFile} file The encrypted file information taken from the matrix event.
 *   This passed to [link]{@link https://github.com/matrix-org/browser-encrypt-attachments}
 *   as the encryption info object, so will also have the those keys in addition to
 *   the keys below.
 * @param {IMediaEventInfo} info The info parameter taken from the matrix event.
 * @returns {Promise<Blob>} Resolves to a Blob of the file.
 */
export function decryptFile(
    file: IEncryptedFile,
    info?: IMediaEventInfo,
): Promise<Blob> {
    const media = mediaFromContent({ file });
    // Download the encrypted file as an array buffer.
    return media.downloadSource().then((response) => {
        return response.arrayBuffer();
    }).then((responseData) => {
        // Decrypt the array buffer using the information taken from
        // the event content.
        return encrypt.decryptAttachment(responseData, file);
    }).then((dataArray) => {
        // Turn the array into a Blob and give it the correct MIME-type.

        // IMPORTANT: we must not allow scriptable mime-types into Blobs otherwise
        // they introduce XSS attacks if the Blob URI is viewed directly in the
        // browser (e.g. by copying the URI into a new tab or window.)
        // See warning at top of file.
        let mimetype = info?.mimetype ? info.mimetype.split(";")[0].trim() : '';
        mimetype = getBlobSafeMimeType(mimetype);

        return new Blob([dataArray], { type: mimetype });
    });
}
