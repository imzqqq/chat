/**
 * Verification method that is illegal to have (cannot possibly
 * do verification with this method).
 * @module crypto/verification/IllegalMethod
 */

import { VerificationBase as Base } from "./Base";
import { IVerificationChannel } from "./request/Channel";
import { MatrixClient } from "../../client";
import { MatrixEvent } from "../../models/event";
import { VerificationRequest } from "./request/VerificationRequest";

/**
 * @class crypto/verification/IllegalMethod/IllegalMethod
 * @extends {module:crypto/verification/Base}
 */
export class IllegalMethod extends Base {
    public static factory(
        channel: IVerificationChannel,
        baseApis: MatrixClient,
        userId: string,
        deviceId: string,
        startEvent: MatrixEvent,
        request: VerificationRequest,
    ): IllegalMethod {
        return new IllegalMethod(channel, baseApis, userId, deviceId, startEvent, request);
    }

    // eslint-disable-next-line @typescript-eslint/naming-convention
    public static get NAME(): string {
        // Typically the name will be something else, but to complete
        // the contract we offer a default one here.
        return "org.matrix.illegal_method";
    }

    protected doVerification = async (): Promise<void> => {
        throw new Error("Verification is not possible with this method");
    };
}
