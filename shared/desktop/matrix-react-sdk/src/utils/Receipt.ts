import { MatrixEvent } from "matrix-js-sdk/src/models/event";

/**
 * Given MatrixEvent containing receipts, return the first
 * read receipt from the given user ID, or null if no such
 * receipt exists.
 *
 * @param {Object} receiptEvent A Chat Event
 * @param {string} userId A user ID
 * @returns {Object} Read receipt
 */
export function findReadReceiptFromUserId(receiptEvent: MatrixEvent, userId: string): object | null {
    const receiptKeys = Object.keys(receiptEvent.getContent());
    for (let i = 0; i < receiptKeys.length; ++i) {
        const rcpt = receiptEvent.getContent()[receiptKeys[i]];
        if (rcpt['m.read'] && rcpt['m.read'][userId]) {
            return rcpt;
        }
    }

    return null;
}
