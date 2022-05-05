import React from "react";

import { _t } from '../languageHandler';
import { MatrixClientPeg } from '../MatrixClientPeg';
import RoomProvider from "./RoomProvider";

export default class SpaceProvider extends RoomProvider {
    protected getRooms() {
        return MatrixClientPeg.get().getVisibleRooms().filter(r => r.isSpaceRoom());
    }

    getName() {
        return _t("Spaces");
    }

    renderCompletions(completions: React.ReactNode[]): React.ReactNode {
        return (
            <div
                className="mx_Autocomplete_Completion_container_pill mx_Autocomplete_Completion_container_truncate"
                role="listbox"
                aria-label={_t("Space Autocomplete")}
            >
                { completions }
            </div>
        );
    }
}
