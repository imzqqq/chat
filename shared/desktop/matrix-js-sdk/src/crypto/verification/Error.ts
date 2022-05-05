/**
 * Error messages.
 *
 * @module crypto/verification/Error
 */

import { MatrixEvent } from "../../models/event";

export function newVerificationError(code: string, reason: string, extraData: Record<string, any>): MatrixEvent {
    const content = Object.assign({}, { code, reason }, extraData);
    return new MatrixEvent({
        type: "m.key.verification.cancel",
        content,
    });
}

export function errorFactory(code: string, reason: string): (extraData?: Record<string, any>) => MatrixEvent {
    return function(extraData?: Record<string, any>) {
        return newVerificationError(code, reason, extraData);
    };
}

/**
 * The verification was cancelled by the user.
 */
export const newUserCancelledError = errorFactory("m.user", "Cancelled by user");

/**
 * The verification timed out.
 */
export const newTimeoutError = errorFactory("m.timeout", "Timed out");

/**
 * The transaction is unknown.
 */
export const newUnknownTransactionError = errorFactory(
    "m.unknown_transaction", "Unknown transaction",
);

/**
 * An unknown method was selected.
 */
export const newUnknownMethodError = errorFactory("m.unknown_method", "Unknown method");

/**
 * An unexpected message was sent.
 */
export const newUnexpectedMessageError = errorFactory(
    "m.unexpected_message", "Unexpected message",
);

/**
 * The key does not match.
 */
export const newKeyMismatchError = errorFactory(
    "m.key_mismatch", "Key mismatch",
);

/**
 * The user does not match.
 */
export const newUserMismatchError = errorFactory("m.user_error", "User mismatch");

/**
 * An invalid message was sent.
 */
export const newInvalidMessageError = errorFactory(
    "m.invalid_message", "Invalid message",
);

export function errorFromEvent(event: MatrixEvent): { code: string, reason: string } {
    const content = event.getContent();
    if (content) {
        const { code, reason } = content;
        return { code, reason };
    } else {
        return { code: "Unknown error", reason: "m.unknown" };
    }
}
