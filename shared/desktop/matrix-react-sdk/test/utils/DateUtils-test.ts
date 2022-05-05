import { formatSeconds } from "../../src/DateUtils";

describe("formatSeconds", () => {
    it("correctly formats time with hours", () => {
        expect(formatSeconds((60 * 60 * 3) + (60 * 31) + (55))).toBe("03:31:55");
        expect(formatSeconds((60 * 60 * 3) + (60 * 0) + (55))).toBe("03:00:55");
        expect(formatSeconds((60 * 60 * 3) + (60 * 31) + (0))).toBe("03:31:00");
    });

    it("correctly formats time without hours", () => {
        expect(formatSeconds((60 * 60 * 0) + (60 * 31) + (55))).toBe("31:55");
        expect(formatSeconds((60 * 60 * 0) + (60 * 0) + (55))).toBe("00:55");
        expect(formatSeconds((60 * 60 * 0) + (60 * 31) + (0))).toBe("31:00");
    });
});
