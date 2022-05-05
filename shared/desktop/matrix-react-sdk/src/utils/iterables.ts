import { arrayDiff, arrayUnion } from "./arrays";

export function iterableUnion<T>(a: Iterable<T>, b: Iterable<T>): Iterable<T> {
    return arrayUnion(Array.from(a), Array.from(b));
}

export function iterableDiff<T>(a: Iterable<T>, b: Iterable<T>): { added: Iterable<T>, removed: Iterable<T> } {
    return arrayDiff(Array.from(a), Array.from(b));
}
