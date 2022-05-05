import React, { useContext, useEffect } from "react";
import { Room } from "matrix-js-sdk/src/models/room";

import MatrixClientContext from "../../../contexts/MatrixClientContext";
import BaseCard from "./BaseCard";
import WidgetUtils from "../../../utils/WidgetUtils";
import AppTile from "../elements/AppTile";
import { _t } from "../../../languageHandler";
import { useWidgets } from "./RoomSummaryCard";
import { RightPanelPhases } from "../../../stores/RightPanelStorePhases";
import defaultDispatcher from "../../../dispatcher/dispatcher";
import { SetRightPanelPhasePayload } from "../../../dispatcher/payloads/SetRightPanelPhasePayload";
import { Action } from "../../../dispatcher/actions";
import { ChevronFace, ContextMenuButton, useContextMenu } from "../../structures/ContextMenu";
import WidgetContextMenu from "../context_menus/WidgetContextMenu";
import { Container, WidgetLayoutStore } from "../../../stores/widgets/WidgetLayoutStore";
import UIStore from "../../../stores/UIStore";

interface IProps {
    room: Room;
    widgetId: string;
    onClose(): void;
}

const WidgetCard: React.FC<IProps> = ({ room, widgetId, onClose }) => {
    const cli = useContext(MatrixClientContext);

    const apps = useWidgets(room);
    const app = apps.find(a => a.id === widgetId);
    const isPinned = app && WidgetLayoutStore.instance.isInContainer(room, app, Container.Top);

    const [menuDisplayed, handle, openMenu, closeMenu] = useContextMenu();

    useEffect(() => {
        if (!app || isPinned) {
            // stop showing this card
            defaultDispatcher.dispatch<SetRightPanelPhasePayload>({
                action: Action.SetRightPanelPhase,
                phase: RightPanelPhases.RoomSummary,
            });
        }
    }, [app, isPinned]);

    // Don't render anything as we are about to transition
    if (!app || isPinned) return null;

    let contextMenu;
    if (menuDisplayed) {
        const rect = handle.current.getBoundingClientRect();
        contextMenu = (
            <WidgetContextMenu
                chevronFace={ChevronFace.None}
                right={UIStore.instance.windowWidth - rect.right - 12}
                top={rect.bottom + 12}
                onFinished={closeMenu}
                app={app}
            />
        );
    }

    const header = <React.Fragment>
        <h2>{ WidgetUtils.getWidgetName(app) }</h2>
        <ContextMenuButton
            kind="secondary"
            className="mx_WidgetCard_optionsButton"
            inputRef={handle}
            onClick={openMenu}
            isExpanded={menuDisplayed}
            label={_t("Options")}
        />
        { contextMenu }
    </React.Fragment>;

    return <BaseCard
        header={header}
        className="mx_WidgetCard"
        onClose={onClose}
        previousPhase={RightPanelPhases.RoomSummary}
        withoutScrollContainer
    >
        <AppTile
            app={app}
            fullWidth
            show
            showMenubar={false}
            room={room}
            userId={cli.getUserId()}
            creatorUserId={app.creatorUserId}
            widgetPageTitle={WidgetUtils.getWidgetDataTitle(app)}
            waitForIframeLoad={app.waitForIframeLoad}
        />
    </BaseCard>;
};

export default WidgetCard;
