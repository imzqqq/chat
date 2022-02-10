import { NamespacedValue, UnstableValue } from "../../src/NamespacedValue";

describe("NamespacedValue", () => {
    it("should prefer stable over unstable", () => {
        const ns = new NamespacedValue("stable", "unstable");
        expect(ns.name).toBe(ns.stable);
        expect(ns.altName).toBe(ns.unstable);
    });

    it("should return unstable if there is no stable", () => {
        const ns = new NamespacedValue(null, "unstable");
        expect(ns.name).toBe(ns.unstable);
        expect(ns.altName).toBeFalsy();
    });

    it("should have a falsey unstable if needed", () => {
        const ns = new NamespacedValue("stable", null);
        expect(ns.name).toBe(ns.stable);
        expect(ns.altName).toBeFalsy();
    });

    it("should match against either stable or unstable", () => {
        const ns = new NamespacedValue("stable", "unstable");
        expect(ns.matches("no")).toBe(false);
        expect(ns.matches(ns.stable)).toBe(true);
        expect(ns.matches(ns.unstable)).toBe(true);
    });

    it("should not permit falsey values for both parts", () => {
        try {
            new UnstableValue(null, null);
            // noinspection ExceptionCaughtLocallyJS
            throw new Error("Failed to fail");
        } catch (e) {
            expect(e.message).toBe("One of stable or unstable values must be supplied");
        }
    });
});

describe("UnstableValue", () => {
    it("should prefer unstable over stable", () => {
        const ns = new UnstableValue("stable", "unstable");
        expect(ns.name).toBe(ns.unstable);
        expect(ns.altName).toBe(ns.stable);
    });

    it("should return unstable if there is no stable", () => {
        const ns = new UnstableValue(null, "unstable");
        expect(ns.name).toBe(ns.unstable);
        expect(ns.altName).toBeFalsy();
    });

    it("should not permit falsey unstable values", () => {
        try {
            new UnstableValue("stable", null);
            // noinspection ExceptionCaughtLocallyJS
            throw new Error("Failed to fail");
        } catch (e) {
            expect(e.message).toBe("Unstable value must be supplied");
        }
    });
});
