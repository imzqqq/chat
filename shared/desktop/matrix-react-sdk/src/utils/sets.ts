/**
 * Determines if two sets are different through a shallow comparison.
 * @param a The first set. Must be defined.
 * @param b The second set. Must be defined.
 * @returns True if they are different, false otherwise.
 */
export function setHasDiff<T>(a: Set<T>, b: Set<T>): boolean {
    if (a.size === b.size) {
        // When the lengths are equal, check to see if either set is missing an element from the other.
        if (Array.from(b).some(i => !a.has(i))) return true;
        if (Array.from(a).some(i => !b.has(i))) return true;

        // if all the keys are common, say so
        return false;
    } else {
        return true; // different lengths means they are naturally diverged
    }
}
