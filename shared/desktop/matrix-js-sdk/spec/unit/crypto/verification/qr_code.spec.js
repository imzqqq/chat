import "../../../olm-loader";
import { logger } from "../../../../src/logger";

const Olm = global.Olm;

describe("QR code verification", function() {
    if (!global.Olm) {
        logger.warn('Not running device verification tests: libolm not present');
        return;
    }

    beforeAll(function() {
        return Olm.init();
    });

    describe("reciprocate", () => {
        it("should verify the secret", () => {
            // TODO: Actually write a test for this.
            // Tests are hard because we are running before the verification
            // process actually begins, and are largely UI-driven rather than
            // logic-driven (compared to something like SAS). In the interest
            // of time, tests are currently excluded.
        });
    });
});
