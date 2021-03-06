import { arrayDiff, arrayMerge, arrayUnion } from "./arrays";

/**
 * Determines the keys added, changed, and removed between two Maps.
 * For changes, simple triple equal comparisons are done, not in-depth tree checking.
 * @param a The first Map. Must be defined.
 * @param b The second Map. Must be defined.
 * @returns The difference between the keys of each Map.
 */
export function mapDiff<K, V>(a: Map<K, V>, b: Map<K, V>): { changed: K[], added: K[], removed: K[] } {
    const aKeys = [...a.keys()];
    const bKeys = [...b.keys()];
    const keyDiff = arrayDiff(aKeys, bKeys);
    const possibleChanges = arrayUnion(aKeys, bKeys);
    const changes = possibleChanges.filter(k => a.get(k) !== b.get(k));

    return { changed: changes, added: keyDiff.added, removed: keyDiff.removed };
}

/**
 * Gets all the key changes (added, removed, or value difference) between two Maps.
 * Triple equals is used to compare values, not in-depth tree checking.
 * @param a The first Map. Must be defined.
 * @param b The second Map. Must be defined.
 * @returns The keys which have been added, removed, or changed between the two Maps.
 */
export function mapKeyChanges<K, V>(a: Map<K, V>, b: Map<K, V>): K[] {
    const diff = mapDiff(a, b);
    return arrayMerge(diff.removed, diff.added, diff.changed);
}

/**
 * A Map<K, V> with added utility.
 */
export class EnhancedMap<K, V> extends Map<K, V> {
    public constructor(entries?: Iterable<[K, V]>) {
        super(entries);
    }

    public getOrCreate(key: K, def: V): V {
        if (this.has(key)) {
            return this.get(key);
        }
        this.set(key, def);
        return def;
    }

    public remove(key: K): V {
        const v = this.get(key);
        this.delete(key);
        return v;
    }
}
