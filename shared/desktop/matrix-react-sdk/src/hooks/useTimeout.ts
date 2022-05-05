import { useEffect, useRef, useState } from "react";

type Handler = () => void;

// Hook to simplify timeouts in functional components
export const useTimeout = (handler: Handler, timeoutMs: number) => {
    // Create a ref that stores handler
    const savedHandler = useRef<Handler>();

    // Update ref.current value if handler changes.
    useEffect(() => {
        savedHandler.current = handler;
    }, [handler]);

    // Set up timer
    useEffect(() => {
        const timeoutID = setTimeout(() => {
            savedHandler.current();
        }, timeoutMs);
        return () => clearTimeout(timeoutID);
    }, [timeoutMs]);
};

// Hook to simplify intervals in functional components
export const useInterval = (handler: Handler, intervalMs: number) => {
    // Create a ref that stores handler
    const savedHandler = useRef<Handler>();

    // Update ref.current value if handler changes.
    useEffect(() => {
        savedHandler.current = handler;
    }, [handler]);

    // Set up timer
    useEffect(() => {
        const intervalID = setInterval(() => {
            savedHandler.current();
        }, intervalMs);
        return () => clearInterval(intervalID);
    }, [intervalMs]);
};

// Hook to simplify a variable counting down to 0, handler called when it reached 0
export const useExpiringCounter = (handler: Handler, intervalMs: number, initialCount: number) => {
    const [count, setCount] = useState(initialCount);
    useInterval(() => setCount(c => c - 1), intervalMs);
    if (count === 0) {
        handler();
    }
    return count;
};
