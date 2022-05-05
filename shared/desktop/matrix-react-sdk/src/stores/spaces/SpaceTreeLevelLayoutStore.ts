const getSpaceCollapsedKey = (roomId: string, parents: Set<string>): string => {
    const separator = "/";
    let path = "";
    if (parents) {
        for (const entry of parents.entries()) {
            path += entry + separator;
        }
    }
    return `mx_space_collapsed_${path + roomId}`;
};

export default class SpaceTreeLevelLayoutStore {
    private static internalInstance: SpaceTreeLevelLayoutStore;

    public static get instance(): SpaceTreeLevelLayoutStore {
        if (!SpaceTreeLevelLayoutStore.internalInstance) {
            SpaceTreeLevelLayoutStore.internalInstance = new SpaceTreeLevelLayoutStore();
        }
        return SpaceTreeLevelLayoutStore.internalInstance;
    }

    public setSpaceCollapsedState(roomId: string, parents: Set<string>, collapsed: boolean) {
        // XXX: localStorage doesn't allow booleans
        localStorage.setItem(getSpaceCollapsedKey(roomId, parents), collapsed.toString());
    }

    public getSpaceCollapsedState(roomId: string, parents: Set<string>, fallback: boolean): boolean {
        const collapsedLocalStorage = localStorage.getItem(getSpaceCollapsedKey(roomId, parents));
        // XXX: localStorage doesn't allow booleans
        return collapsedLocalStorage ? collapsedLocalStorage === "true" : fallback;
    }
}
