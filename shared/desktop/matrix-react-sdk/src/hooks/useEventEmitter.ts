import { useRef, useEffect, useState, useCallback } from "react";
import type { EventEmitter } from "events";

type Handler = (...args: any[]) => void;

// Hook to wrap event emitter on and removeListener in hook lifecycle
export const useEventEmitter = (
    emitter: EventEmitter | undefined,
    eventName: string | symbol,
    handler: Handler,
) => {
    // Create a ref that stores handler
    const savedHandler = useRef(handler);

    // Update ref.current value if handler changes.
    useEffect(() => {
        savedHandler.current = handler;
    }, [handler]);

    useEffect(
        () => {
            // allow disabling this hook by passing a falsy emitter
            if (!emitter) return;

            // Create event listener that calls handler function stored in ref
            const eventListener = (...args) => savedHandler.current(...args);

            // Add event listener
            emitter.on(eventName, eventListener);

            // Remove event listener on cleanup
            return () => {
                emitter.removeListener(eventName, eventListener);
            };
        },
        [eventName, emitter], // Re-run if eventName or emitter changes
    );
};

type Mapper<T> = (...args: any[]) => T;

export const useEventEmitterState = <T>(
    emitter: EventEmitter | undefined,
    eventName: string | symbol,
    fn: Mapper<T>,
): T => {
    const [value, setValue] = useState<T>(fn());
    const handler = useCallback((...args: any[]) => {
        setValue(fn(...args));
    }, [fn]);
    useEventEmitter(emitter, eventName, handler);
    return value;
};
