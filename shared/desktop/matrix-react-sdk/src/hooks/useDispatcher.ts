import { useEffect, useRef } from "react";

import { ActionPayload } from "../dispatcher/payloads";
import { Dispatcher } from "flux";

// Hook to simplify listening to flux dispatches
export const useDispatcher = (dispatcher: Dispatcher<ActionPayload>, handler: (payload: ActionPayload) => void) => {
    // Create a ref that stores handler
    const savedHandler = useRef((payload: ActionPayload) => {});

    // Update ref.current value if handler changes.
    useEffect(() => {
        savedHandler.current = handler;
    }, [handler]);

    useEffect(() => {
        // Create event listener that calls handler function stored in ref
        const ref = dispatcher.register((payload) => savedHandler.current(payload));
        // Remove event listener on cleanup
        return () => {
            dispatcher.unregister(ref);
        };
    }, [dispatcher]);
};
