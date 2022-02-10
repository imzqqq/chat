/**
 * Represents a simple Chat namespaced value. This will assume that if a stable prefix
 * is provided that the stable prefix should be used when representing the identifier.
 */
export class NamespacedValue<S extends string, U extends string> {
    // Stable is optional, but one of the two parameters is required, hence the weird-looking types.
    // Goal is to to have developers explicitly say there is no stable value (if applicable).
    public constructor(public readonly stable: S | null | undefined, public readonly unstable?: U) {
        if (!this.unstable && !this.stable) {
            throw new Error("One of stable or unstable values must be supplied");
        }
    }

    public get name(): U | S {
        if (this.stable) {
            return this.stable;
        }
        return this.unstable;
    }

    public get altName(): U | S | null {
        if (!this.stable) {
            return null;
        }
        return this.unstable;
    }

    public matches(val: string): boolean {
        return this.name === val || this.altName === val;
    }

    // this desperately wants https://github.com/microsoft/TypeScript/pull/26349 at the top level of the class
    // so we can instantiate `NamespacedValue<string, _, _>` as a default type for that namespace.
    public findIn<T>(obj: any): T {
        let val: T;
        if (this.name) {
            val = obj?.[this.name];
        }
        if (!val && this.altName) {
            val = obj?.[this.altName];
        }
        return val;
    }

    public includedIn(arr: any[]): boolean {
        let included = false;
        if (this.name) {
            included = arr.includes(this.name);
        }
        if (!included && this.altName) {
            included = arr.includes(this.altName);
        }
        return included;
    }
}

/**
 * Represents a namespaced value which prioritizes the unstable value over the stable
 * value.
 */
export class UnstableValue<S extends string, U extends string> extends NamespacedValue<S, U> {
    // Note: Constructor difference is that `unstable` is *required*.
    public constructor(stable: S, unstable: U) {
        super(stable, unstable);
        if (!this.unstable) {
            throw new Error("Unstable value must be supplied");
        }
    }

    public get name(): U {
        return this.unstable;
    }

    public get altName(): S {
        return this.stable;
    }
}
