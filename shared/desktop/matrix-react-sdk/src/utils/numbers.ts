/**
 * Returns the default number if the given value, i, is not a number. Otherwise
 * returns the given value.
 * @param {*} i The value to check.
 * @param {number} def The default value.
 * @returns {number} Either the value or the default value, whichever is a number.
 */
export function defaultNumber(i: unknown, def: number): number {
    return Number.isFinite(i) ? Number(i) : def;
}

export function clamp(i: number, min: number, max: number): number {
    return Math.min(Math.max(i, min), max);
}

export function sum(...i: number[]): number {
    return [...i].reduce((p, c) => c + p, 0);
}

export function percentageWithin(pct: number, min: number, max: number): number {
    return (pct * (max - min)) + min;
}

export function percentageOf(val: number, min: number, max: number): number {
    return (val - min) / (max - min);
}
