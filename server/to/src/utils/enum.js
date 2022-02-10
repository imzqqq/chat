export function createEnum(...values) {
    const obj = {};
    for (const value of values) {
        if (typeof value !== "string") {
            throw new Error("Invalid enum value name" + value?.toString());
        }
        obj[value] = value;
    }
    return Object.freeze(obj);
}
