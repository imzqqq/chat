import globToRegexp from "glob-to-regexp";

// Taken with permission from matrix-bot-sdk:
// https://github.com/turt2live/matrix-js-bot-sdk/blob/eb148c2ecec7bf3ade801d73deb43df042d55aef/src/MatrixGlob.ts

/**
 * Represents a common Chat glob. This is commonly used
 * for server ACLs and similar functions.
 */
export class MatrixGlob {
    _regex: RegExp;

    /**
     * Creates a new Chat Glob
     * @param {string} glob The glob to convert. Eg: "*.example.org"
     */
    constructor(glob: string) {
        const globRegex = globToRegexp(glob, {
            extended: false,
            globstar: false,
        });

        // We need to convert `?` manually because globToRegexp's extended mode
        // does more than we want it to.
        const replaced = globRegex.toString().replace(/\\\?/g, ".");
        this._regex = new RegExp(replaced.substring(1, replaced.length - 1));
    }

    /**
     * Tests the glob against a value, returning true if it matches.
     * @param {string} val The value to test.
     * @returns {boolean} True if the value matches the glob, false otherwise.
     */
    test(val: string): boolean {
        return this._regex.test(val);
    }
}
