/* eslint new-cap: "off" */

import TagTile from './TagTile';

import React from 'react';
import { ContextMenu, toRightOf, useContextMenu } from "../../structures/ContextMenu";
import * as sdk from '../../../index';

export default function DNDTagTile(props) {
    const [menuDisplayed, handle, openMenu, closeMenu] = useContextMenu();

    let contextMenu = null;
    if (menuDisplayed && handle.current) {
        const elementRect = handle.current.getBoundingClientRect();
        const TagTileContextMenu = sdk.getComponent('context_menus.TagTileContextMenu');
        contextMenu = (
            <ContextMenu {...toRightOf(elementRect)} onFinished={closeMenu}>
                <TagTileContextMenu tag={props.tag} onFinished={closeMenu} index={props.index} />
            </ContextMenu>
        );
    }
    return <>
        <TagTile
            {...props}
            contextMenuButtonRef={handle}
            menuDisplayed={menuDisplayed}
            openMenu={openMenu}
        />
        { contextMenu }
    </>;
}
