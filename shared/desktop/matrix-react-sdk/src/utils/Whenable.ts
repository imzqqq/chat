import { IDestroyable } from "./IDestroyable";
import { arrayFastClone } from "./arrays";

export type WhenFn<T> = (w: Whenable<T>) => void;

/**
 * Whenables are a cheap way to have Observable patterns mixed with typical
 * usage of Promises, without having to tear down listeners or calls. Whenables
 * are intended to be used when a condition will be met multiple times and
 * the consumer needs to know *when* that happens.
 */
export abstract class Whenable<T> implements IDestroyable {
    private listeners: {condition: T | null, fn: WhenFn<T>}[] = [];

    /**
     * Sets up a call to `fn` *when* the `condition` is met.
     * @param condition The condition to match.
     * @param fn The function to call.
     * @returns This.
     */
    public when(condition: T, fn: WhenFn<T>): Whenable<T> {
        this.listeners.push({ condition, fn });
        return this;
    }

    /**
     * Sets up a call to `fn` *when* any of the `conditions` are met.
     * @param conditions The conditions to match.
     * @param fn The function to call.
     * @returns This.
     */
    public whenAnyOf(conditions: T[], fn: WhenFn<T>): Whenable<T> {
        for (const condition of conditions) {
            this.when(condition, fn);
        }
        return this;
    }

    /**
     * Sets up a call to `fn` *when* any condition is met.
     * @param fn The function to call.
     * @returns This.
     */
    public whenAnything(fn: WhenFn<T>): Whenable<T> {
        this.listeners.push({ condition: null, fn });
        return this;
    }

    /**
     * Notifies all the listeners of a given condition.
     * @param condition The new condition that has been met.
     */
    protected notifyCondition(condition: T) {
        const listeners = arrayFastClone(this.listeners); // clone just in case the handler modifies us
        for (const listener of listeners) {
            if (listener.condition === null || listener.condition === condition) {
                try {
                    listener.fn(this);
                } catch (e) {
                    console.error(`Error calling whenable listener for ${condition}:`, e);
                }
            }
        }
    }

    public destroy() {
        this.listeners = [];
    }
}
