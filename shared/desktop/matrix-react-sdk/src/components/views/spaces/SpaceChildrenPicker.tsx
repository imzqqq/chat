import React, { useEffect, useMemo, useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";

import { _t } from "../../../languageHandler";
import StyledRadioGroup from "../elements/StyledRadioGroup";
import QueryMatcher from "../../../autocomplete/QueryMatcher";
import SearchBox from "../../structures/SearchBox";
import AutoHideScrollbar from "../../structures/AutoHideScrollbar";
import { Entry } from "../dialogs/AddExistingToSpaceDialog";

enum Target {
    All = "All",
    Specific = "Specific",
    None = "None",
}

interface ISpecificChildrenPickerProps {
    filterPlaceholder: string;
    rooms: Room[];
    selected: Set<Room>;
    onChange(selected: boolean, room: Room): void;
}

const SpecificChildrenPicker = ({ filterPlaceholder, rooms, selected, onChange }: ISpecificChildrenPickerProps) => {
    const [query, setQuery] = useState("");
    const lcQuery = query.toLowerCase().trim();

    const filteredRooms = useMemo(() => {
        if (!lcQuery) {
            return rooms;
        }

        const matcher = new QueryMatcher<Room>(rooms, {
            keys: ["name"],
            funcs: [r => [r.getCanonicalAlias(), ...r.getAltAliases()].filter(Boolean)],
            shouldMatchWordsOnly: false,
        });

        return matcher.match(lcQuery);
    }, [rooms, lcQuery]);

    return <div className="mx_SpaceChildrenPicker">
        <SearchBox
            className="mx_textinput_icon mx_textinput_search"
            placeholder={filterPlaceholder}
            onSearch={setQuery}
            autoFocus={true}
        />
        <AutoHideScrollbar>
            { filteredRooms.map(room => {
                return <Entry
                    key={room.roomId}
                    room={room}
                    checked={selected.has(room)}
                    onChange={(checked) => {
                        onChange(checked, room);
                    }}
                />;
            }) }
            { filteredRooms.length < 1 ? <span className="mx_SpaceChildrenPicker_noResults">
                { _t("No results") }
            </span> : undefined }
        </AutoHideScrollbar>
    </div>;
};

interface IProps {
    space: Room;
    spaceChildren: Room[];
    selected: Set<Room>;
    noneLabel?: string;
    allLabel: string;
    specificLabel: string;
    onChange(rooms: Room[]): void;
}

const SpaceChildrenPicker = ({
    space,
    spaceChildren,
    selected,
    onChange,
    noneLabel,
    allLabel,
    specificLabel,
}: IProps) => {
    const [state, setState] = useState<string>(noneLabel ? Target.None : Target.All);

    useEffect(() => {
        if (state === Target.All) {
            onChange(spaceChildren);
        } else {
            onChange([]);
        }
    }, [onChange, state, spaceChildren]);

    return <>
        <div className="mx_SpaceChildrenPicker">
            <StyledRadioGroup
                name="roomsToLeave"
                value={state}
                onChange={setState}
                definitions={[
                    {
                        value: Target.None,
                        label: noneLabel,
                    }, {
                        value: Target.All,
                        label: allLabel,
                    }, {
                        value: Target.Specific,
                        label: specificLabel,
                    },
                ].filter(d => d.label)}
            />
        </div>

        { state === Target.Specific && (
            <SpecificChildrenPicker
                filterPlaceholder={_t("Search %(spaceName)s", { spaceName: space.name })}
                rooms={spaceChildren}
                selected={selected}
                onChange={(isSelected: boolean, room: Room) => {
                    if (isSelected) {
                        onChange([room, ...selected]);
                    } else {
                        onChange([...selected].filter(r => r !== room));
                    }
                }}
            />
        ) }
    </>;
};

export default SpaceChildrenPicker;
