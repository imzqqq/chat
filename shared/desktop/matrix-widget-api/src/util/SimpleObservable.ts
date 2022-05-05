export type ObservableFunction<T> = (val: T) => void;

export class SimpleObservable<T> {
    private listeners: ObservableFunction<T>[] = [];

    public constructor(initialFn?: ObservableFunction<T>) {
        if (initialFn) this.listeners.push(initialFn);
    }

    public onUpdate(fn: ObservableFunction<T>) {
        this.listeners.push(fn);
    }

    public update(val: T) {
        for (const listener of this.listeners) {
            listener(val);
        }
    }

    public close() {
        this.listeners = []; // reset
    }
}
