function requireCanonicalAliasAccessToPublish(): boolean {
    // Some environments may not care about this requirement and could return false
    return true;
}

// This interface summarises all available customisation points and also marks
// them all as optional. This allows customisers to only define and export the
// customisations they need while still maintaining type safety.
export interface IDirectoryCustomisations {
    requireCanonicalAliasAccessToPublish?: typeof requireCanonicalAliasAccessToPublish;
}

// A real customisation module will define and export one or more of the
// customisation points that make up `IDirectoryCustomisations`.
export default {} as IDirectoryCustomisations;
