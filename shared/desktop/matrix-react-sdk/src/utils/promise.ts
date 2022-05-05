// Returns a promise which resolves when the input promise resolves with its value
// or when the timeout of ms is reached with the value of given timeoutValue
export async function timeout<T>(promise: Promise<T>, timeoutValue: T, ms: number): Promise<T> {
    const timeoutPromise = new Promise<T>((resolve) => {
        const timeoutId = setTimeout(resolve, ms, timeoutValue);
        promise.then(() => {
            clearTimeout(timeoutId);
        });
    });

    return Promise.race([promise, timeoutPromise]);
}

// Helper method to retry a Promise a given number of times or until a predicate fails
export async function retry<T, E extends Error>(fn: () => Promise<T>, num: number, predicate?: (e: E) => boolean) {
    let lastErr: E;
    for (let i = 0; i < num; i++) {
        try {
            const v = await fn();
            // If `await fn()` throws then we won't reach here
            return v;
        } catch (err) {
            if (predicate && !predicate(err)) {
                throw err;
            }
            lastErr = err;
        }
    }
    throw lastErr;
}
