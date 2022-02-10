/**
 * Utility class for lazily getting a variable.
 */
export class LazyValue<T> {
    private val: T;
    private prom: Promise<T>;
    private done = false;

    public constructor(private getFn: () => Promise<T>) {
    }

    /**
     * Whether or not a cached value is present.
     */
    public get present(): boolean {
        // we use a tracking variable just in case the final value is falsey
        return this.done;
    }

    /**
     * Gets the value without invoking a get. May be undefined until the
     * value is fetched properly.
     */
    public get cachedValue(): T {
        return this.val;
    }

    /**
     * Gets a promise which resolves to the value, eventually.
     */
    public get value(): Promise<T> {
        if (this.prom) return this.prom;
        this.prom = this.getFn();

        // Fork the promise chain to avoid accidentally making it return undefined always.
        this.prom.then(v => {
            this.val = v;
            this.done = true;
        });

        return this.prom;
    }
}
