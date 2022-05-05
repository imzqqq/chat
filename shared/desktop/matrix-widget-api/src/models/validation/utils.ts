export function assertPresent<O>(obj: O, key: keyof O) {
    if (!obj[key]) {
        throw new Error(`${key} is required`);
    }
}
