/**
 * This is meant to be a cache of room alias to room ID so that moving between
 * rooms happens smoothly (for example using browser back / forward buttons).
 *
 * For the moment, it's in memory only and so only applies for the current
 * session for simplicity, but could be extended further in the future.
 *
 * A similar thing could also be achieved via `pushState` with a state object,
 * but keeping it separate like this seems easier in case we do want to extend.
 */
const aliasToIDMap = new Map<string, string>();

export function storeRoomAliasInCache(alias: string, id: string): void {
    aliasToIDMap.set(alias, id);
}

export function getCachedRoomIDForAlias(alias: string): string {
    return aliasToIDMap.get(alias);
}
