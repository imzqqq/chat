import React from 'react';
import { Room } from "matrix-js-sdk/src/models/room";

import AutocompleteProvider from './AutocompleteProvider';
import { _t } from '../languageHandler';
import { MatrixClientPeg } from '../MatrixClientPeg';
import { PillCompletion } from './Components';
import { ICompletion, ISelectionRange } from "./Autocompleter";
import RoomAvatar from '../components/views/avatars/RoomAvatar';

const AT_ROOM_REGEX = /@\S*/g;

export default class NotifProvider extends AutocompleteProvider {
    room: Room;

    constructor(room) {
        super(AT_ROOM_REGEX);
        this.room = room;
    }

    async getCompletions(
        query: string,
        selection: ISelectionRange,
        force = false,
        limit = -1,
    ): Promise<ICompletion[]> {
        const client = MatrixClientPeg.get();

        if (!this.room.currentState.mayTriggerNotifOfType('room', client.credentials.userId)) return [];

        const { command, range } = this.getCurrentCommand(query, selection, force);
        if (command && command[0] && '@room'.startsWith(command[0]) && command[0].length > 1) {
            return [{
                completion: '@room',
                completionId: '@room',
                type: "at-room",
                suffix: ' ',
                component: (
                    <PillCompletion title="@room" description={_t("Notify the whole room")}>
                        <RoomAvatar width={24} height={24} room={this.room} />
                    </PillCompletion>
                ),
                range,
            }];
        }
        return [];
    }

    getName() {
        return '❗️ ' + _t('Room Notification');
    }

    renderCompletions(completions: React.ReactNode[]): React.ReactNode {
        return (
            <div
                className="mx_Autocomplete_Completion_container_pill mx_Autocomplete_Completion_container_truncate"
                role="presentation"
                aria-label={_t("Notification Autocomplete")}
            >
                { completions }
            </div>
        );
    }
}
