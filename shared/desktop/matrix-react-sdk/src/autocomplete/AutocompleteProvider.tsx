import React from 'react';
import type { ICompletion, ISelectionRange } from './Autocompleter';

export interface ICommand {
    command: string | null;
    range: {
        start: number;
        end: number;
    };
}

export default abstract class AutocompleteProvider {
    commandRegex: RegExp;
    forcedCommandRegex: RegExp;

    protected constructor(commandRegex?: RegExp, forcedCommandRegex?: RegExp) {
        if (commandRegex) {
            if (!commandRegex.global) {
                throw new Error('commandRegex must have global flag set');
            }
            this.commandRegex = commandRegex;
        }
        if (forcedCommandRegex) {
            if (!forcedCommandRegex.global) {
                throw new Error('forcedCommandRegex must have global flag set');
            }
            this.forcedCommandRegex = forcedCommandRegex;
        }
    }

    destroy() {
        // stub
    }

    /**
     * Of the matched commands in the query, returns the first that contains or is contained by the selection, or null.
     * @param {string} query The query string
     * @param {ISelectionRange} selection Selection to search
     * @param {boolean} force True if the user is forcing completion
     * @return {object} { command, range } where both objects fields are null if no match
     */
    getCurrentCommand(query: string, selection: ISelectionRange, force = false) {
        let commandRegex = this.commandRegex;

        if (force && this.shouldForceComplete()) {
            commandRegex = this.forcedCommandRegex || /\S+/g;
        }

        if (!commandRegex) {
            return null;
        }

        commandRegex.lastIndex = 0;

        let match;
        while ((match = commandRegex.exec(query)) !== null) {
            const start = match.index;
            const end = start + match[0].length;
            if (selection.start <= end && selection.end >= start) {
                return {
                    command: match,
                    range: {
                        start,
                        end,
                    },
                };
            }
        }
        return {
            command: null,
            range: {
                start: -1,
                end: -1,
            },
        };
    }

    abstract getCompletions(
        query: string,
        selection: ISelectionRange,
        force: boolean,
        limit: number,
    ): Promise<ICompletion[]>;

    abstract getName(): string;

    abstract renderCompletions(completions: React.ReactNode[]): React.ReactNode | null;

    // Whether we should provide completions even if triggered forcefully, without a sigil.
    shouldForceComplete(): boolean {
        return false;
    }
}
