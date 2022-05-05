import { MatrixClient } from "./client";
import { IEvent, MatrixEvent } from "./models/event";

export type EventMapper = (obj: Partial<IEvent>) => MatrixEvent;

export interface MapperOpts {
    preventReEmit?: boolean;
    decrypt?: boolean;
}

export function eventMapperFor(client: MatrixClient, options: MapperOpts): EventMapper {
    const preventReEmit = Boolean(options.preventReEmit);
    const decrypt = options.decrypt !== false;

    function mapper(plainOldJsObject: Partial<IEvent>) {
        const event = new MatrixEvent(plainOldJsObject);
        if (event.isEncrypted()) {
            if (!preventReEmit) {
                client.reEmitter.reEmit(event, [
                    "Event.decrypted",
                ]);
            }
            if (decrypt) {
                client.decryptEventIfNeeded(event);
            }
        }
        if (!preventReEmit) {
            client.reEmitter.reEmit(event, ["Event.replaced"]);
        }
        return event;
    }

    return mapper;
}
