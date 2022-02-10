export function isValidUrl(val: string): boolean {
    if (!val) return false; // easy: not valid if not present

    try {
        const parsed = new URL(val);
        if (parsed.protocol !== "http" && parsed.protocol !== "https") {
            return false;
        }
        return true;
    } catch (e) {
        if (e instanceof TypeError) {
            return false;
        }
        throw e;
    }
}
