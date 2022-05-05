import React, { useContext, useEffect } from "react";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { IPreviewUrlResponse } from "matrix-js-sdk/src/client";

import { useStateToggle } from "../../../hooks/useStateToggle";
import LinkPreviewWidget from "./LinkPreviewWidget";
import AccessibleButton from "../elements/AccessibleButton";
import { _t } from "../../../languageHandler";
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { useAsyncMemo } from "../../../hooks/useAsyncMemo";

const INITIAL_NUM_PREVIEWS = 2;

interface IProps {
    links: string[]; // the URLs to be previewed
    mxEvent: MatrixEvent; // the Event associated with the preview
    onCancelClick(): void; // called when the preview's cancel ('hide') button is clicked
    onHeightChanged(): void; // called when the preview's contents has loaded
}

const LinkPreviewGroup: React.FC<IProps> = ({ links, mxEvent, onCancelClick, onHeightChanged }) => {
    const cli = useContext(MatrixClientContext);
    const [expanded, toggleExpanded] = useStateToggle();

    const ts = mxEvent.getTs();
    const previews = useAsyncMemo<[string, IPreviewUrlResponse][]>(async () => {
        return Promise.all<[string, IPreviewUrlResponse] | void>(links.map(async link => {
            try {
                return [link, await cli.getUrlPreview(link, ts)];
            } catch (error) {
                console.error("Failed to get URL preview: " + error);
            }
        })).then(a => a.filter(Boolean)) as Promise<[string, IPreviewUrlResponse][]>;
    }, [links, ts], []);

    useEffect(() => {
        onHeightChanged();
    }, [onHeightChanged, expanded, previews]);

    const showPreviews = expanded ? previews : previews.slice(0, INITIAL_NUM_PREVIEWS);

    let toggleButton: JSX.Chat;
    if (previews.length > INITIAL_NUM_PREVIEWS) {
        toggleButton = <AccessibleButton onClick={toggleExpanded}>
            { expanded
                ? _t("Collapse")
                : _t("Show %(count)s other previews", { count: previews.length - showPreviews.length }) }
        </AccessibleButton>;
    }

    return <div className="mx_LinkPreviewGroup">
        { showPreviews.map(([link, preview], i) => (
            <LinkPreviewWidget key={link} link={link} preview={preview} mxEvent={mxEvent}>
                { i === 0 ? (
                    <AccessibleButton
                        className="mx_LinkPreviewGroup_hide"
                        onClick={onCancelClick}
                        aria-label={_t("Close preview")}
                    >
                        <img
                            className="mx_filterFlipColor"
                            alt=""
                            role="presentation"
                            src={require("../../../../res/img/cancel.svg")}
                            width="18"
                            height="18"
                        />
                    </AccessibleButton>
                ): undefined }
            </LinkPreviewWidget>
        )) }
        { toggleButton }
    </div>;
};

export default LinkPreviewGroup;
