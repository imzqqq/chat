import "../../../olm-loader";
import { verificationMethods } from "../../../../src/crypto";
import { logger } from "../../../../src/logger";
import { SAS } from "../../../../src/crypto/verification/SAS";
import { makeTestClients, setupWebcrypto, teardownWebcrypto } from './util';

const Olm = global.Olm;

jest.useFakeTimers();

describe("verification request integration tests with crypto layer", function() {
    if (!global.Olm) {
        logger.warn('Not running device verification unit tests: libolm not present');
        return;
    }

    beforeAll(function() {
        setupWebcrypto();
        return Olm.init();
    });

    afterAll(() => {
        teardownWebcrypto();
    });

    it("should request and accept a verification", async function() {
        const [alice, bob] = await makeTestClients(
            [
                { userId: "@alice:example.com", deviceId: "Osborne2" },
                { userId: "@bob:example.com", deviceId: "Dynabook" },
            ],
            {
                verificationMethods: [verificationMethods.SAS],
            },
        );
        alice.client.crypto.deviceList.getRawStoredDevicesForUser = function() {
            return {
                Dynabook: {
                    keys: {
                        "ed25519:Dynabook": "bob+base64+ed25519+key",
                    },
                },
            };
        };
        alice.client.downloadKeys = () => {
            return Promise.resolve();
        };
        bob.client.downloadKeys = () => {
            return Promise.resolve();
        };
        bob.client.on("crypto.verification.request", (request) => {
            const bobVerifier = request.beginKeyVerification(verificationMethods.SAS);
            bobVerifier.verify();

            // XXX: Private function access (but it's a test, so we're okay)
            bobVerifier.endTimer();
        });
        const aliceRequest = await alice.client.requestVerification("@bob:example.com");
        await aliceRequest.waitFor(r => r.started);
        const aliceVerifier = aliceRequest.verifier;
        expect(aliceVerifier).toBeInstanceOf(SAS);

        // XXX: Private function access (but it's a test, so we're okay)
        aliceVerifier.endTimer();
    });
});
