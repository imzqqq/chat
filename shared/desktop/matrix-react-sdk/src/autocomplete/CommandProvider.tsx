import React from 'react';
import { _t } from '../languageHandler';
import AutocompleteProvider from './AutocompleteProvider';
import QueryMatcher from './QueryMatcher';
import { TextualCompletion } from './Components';
import { ICompletion, ISelectionRange } from "./Autocompleter";
import { Command, Commands, CommandMap } from '../SlashCommands';

const COMMAND_RE = /(^\/\w*)(?: .*)?/g;

export default class CommandProvider extends AutocompleteProvider {
    matcher: QueryMatcher<Command>;

    constructor() {
        super(COMMAND_RE);
        this.matcher = new QueryMatcher(Commands, {
            keys: ['command', 'args', 'description'],
            funcs: [({ aliases }) => aliases.join(" ")], // aliases
        });
    }

    async getCompletions(
        query: string,
        selection: ISelectionRange,
        force?: boolean,
        limit = -1,
    ): Promise<ICompletion[]> {
        const { command, range } = this.getCurrentCommand(query, selection);
        if (!command) return [];

        let matches = [];
        // check if the full match differs from the first word (i.e. returns false if the command has args)
        if (command[0] !== command[1]) {
            // The input looks like a command with arguments, perform exact match
            const name = command[1].substr(1); // strip leading `/`
            if (CommandMap.has(name) && CommandMap.get(name).isEnabled()) {
                // some commands, namely `me` don't suit having the usage shown whilst typing their arguments
                if (CommandMap.get(name).hideCompletionAfterSpace) return [];
                matches = [CommandMap.get(name)];
            }
        } else {
            if (query === '/') {
                // If they have just entered `/` show everything
                // We exclude the limit on purpose to have a comprehensive list
                matches = Commands;
            } else {
                // otherwise fuzzy match against all of the fields
                matches = this.matcher.match(command[1], limit);
            }
        }

        return matches.filter(cmd => cmd.isEnabled()).map((result) => {
            let completion = result.getCommand() + ' ';
            const usedAlias = result.aliases.find(alias => `/${alias}` === command[1]);
            // If the command (or an alias) is the same as the one they entered, we don't want to discard their arguments
            if (usedAlias || result.getCommand() === command[1]) {
                completion = command[0];
            }

            return {
                completion,
                type: "command",
                component: <TextualCompletion
                    title={`/${usedAlias || result.command}`}
                    subtitle={result.args}
                    description={_t(result.description)} />,
                range,
            };
        });
    }

    getName() {
        return '*️⃣ ' + _t('Commands');
    }

    renderCompletions(completions: React.ReactNode[]): React.ReactNode {
        return (
            <div
                className="mx_Autocomplete_Completion_container_pill"
                role="presentation"
                aria-label={_t("Command Autocomplete")}
            >
                { completions }
            </div>
        );
    }
}
