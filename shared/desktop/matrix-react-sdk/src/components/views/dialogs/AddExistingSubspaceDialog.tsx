import React, { useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";

import { _t } from '../../../languageHandler';
import BaseDialog from "./BaseDialog";
import AccessibleButton from "../elements/AccessibleButton";
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { AddExistingToSpace, defaultSpacesRenderer, SubspaceSelector } from "./AddExistingToSpaceDialog";

interface IProps {
    space: Room;
    onCreateSubspaceClick(): void;
    onFinished(added?: boolean): void;
}

const AddExistingSubspaceDialog: React.FC<IProps> = ({ space, onCreateSubspaceClick, onFinished }) => {
    const [selectedSpace, setSelectedSpace] = useState(space);

    return <BaseDialog
        title={(
            <SubspaceSelector
                title={_t("Add existing space")}
                space={space}
                value={selectedSpace}
                onChange={setSelectedSpace}
            />
        )}
        className="mx_AddExistingToSpaceDialog"
        contentId="mx_AddExistingToSpace"
        onFinished={onFinished}
        fixedWidth={false}
    >
        <MatrixClientContext.Provider value={space.client}>
            <AddExistingToSpace
                space={space}
                onFinished={onFinished}
                footerPrompt={<>
                    <div>{ _t("Want to add a new space instead?") }</div>
                    <AccessibleButton onClick={onCreateSubspaceClick} kind="link">
                        { _t("Create a new space") }
                    </AccessibleButton>
                </>}
                filterPlaceholder={_t("Search for spaces")}
                spacesRenderer={defaultSpacesRenderer}
            />
        </MatrixClientContext.Provider>
    </BaseDialog>;
};

export default AddExistingSubspaceDialog;

