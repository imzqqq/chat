function getDisplayAliasForAliasSet(canonicalAlias: string, altAliases: string[]): string {
    // E.g. prefer one of the aliases over another
    return null;
}

// This interface summarises all available customisation points and also marks
// them all as optional. This allows customisers to only define and export the
// customisations they need while still maintaining type safety.
export interface IAliasCustomisations {
    getDisplayAliasForAliasSet?: typeof getDisplayAliasForAliasSet;
}

// A real customisation module will define and export one or more of the
// customisation points that make up `IAliasCustomisations`.
export default {} as IAliasCustomisations;
