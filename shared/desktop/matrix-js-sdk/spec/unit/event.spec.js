import { logger } from "../../src/logger";
import { MatrixEvent } from "../../src/models/event";

describe("MatrixEvent", () => {
    describe(".attemptDecryption", () => {
        let encryptedEvent;

        beforeEach(() => {
            encryptedEvent = new MatrixEvent({
                id: 'test_encrypted_event',
                type: 'm.room.encrypted',
                content: {
                    ciphertext: 'secrets',
                },
            });
        });

        it('should retry decryption if a retry is queued', () => {
            let callCount = 0;

            let prom2;
            let prom2Fulfilled = false;

            const crypto = {
                decryptEvent: function() {
                    ++callCount;
                    logger.log(`decrypt: ${callCount}`);
                    if (callCount == 1) {
                        // schedule a second decryption attempt while
                        // the first one is still running.
                        prom2 = encryptedEvent.attemptDecryption(crypto);
                        prom2.then(() => prom2Fulfilled = true);

                        const error = new Error("nope");
                        error.name = 'DecryptionError';
                        return Promise.reject(error);
                    } else {
                        expect(prom2Fulfilled).toBe(
                            false, 'second attemptDecryption resolved too soon');

                        return Promise.resolve({
                            clearEvent: {
                                type: 'm.room.message',
                            },
                        });
                    }
                },
            };

            return encryptedEvent.attemptDecryption(crypto).then(() => {
                expect(callCount).toEqual(2);
                expect(encryptedEvent.getType()).toEqual('m.room.message');

                // make sure the second attemptDecryption resolves
                return prom2;
            });
        });
    });
});
