import { TagID } from "./models";
import { ListLayout } from "./ListLayout";
import { AsyncStoreWithClient } from "../AsyncStoreWithClient";
import defaultDispatcher from "../../dispatcher/dispatcher";
import { ActionPayload } from "../../dispatcher/payloads";

interface IState {}

export default class RoomListLayoutStore extends AsyncStoreWithClient<IState> {
    private static internalInstance: RoomListLayoutStore;

    private readonly layoutMap = new Map<TagID, ListLayout>();

    constructor() {
        super(defaultDispatcher);
    }

    public static get instance(): RoomListLayoutStore {
        if (!RoomListLayoutStore.internalInstance) {
            RoomListLayoutStore.internalInstance = new RoomListLayoutStore();
        }
        return RoomListLayoutStore.internalInstance;
    }

    public ensureLayoutExists(tagId: TagID) {
        if (!this.layoutMap.has(tagId)) {
            this.layoutMap.set(tagId, new ListLayout(tagId));
        }
    }

    public getLayoutFor(tagId: TagID): ListLayout {
        if (!this.layoutMap.has(tagId)) {
            this.layoutMap.set(tagId, new ListLayout(tagId));
        }
        return this.layoutMap.get(tagId);
    }

    // Note: this primarily exists for debugging, and isn't really intended to be used by anything.
    public async resetLayouts() {
        console.warn("Resetting layouts for room list");
        for (const layout of this.layoutMap.values()) {
            layout.reset();
        }
    }

    protected async onNotReady(): Promise<any> {
        // On logout, clear the map.
        this.layoutMap.clear();
    }

    // We don't need this function, but our contract says we do
    protected async onAction(payload: ActionPayload): Promise<any> {
        return Promise.resolve();
    }
}

window.mxRoomListLayoutStore = RoomListLayoutStore.instance;
