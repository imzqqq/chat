import React, { useContext, useRef, useState } from 'react';
import { EventType } from 'matrix-js-sdk/src/@types/event';
import classNames from 'classnames';

import AccessibleButton from "./AccessibleButton";
import Spinner from "./Spinner";
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { useTimeout } from "../../../hooks/useTimeout";
import Analytics from "../../../Analytics";
import CountlyAnalytics from '../../../CountlyAnalytics';
import RoomContext from "../../../contexts/RoomContext";

export const AVATAR_SIZE = 52;

interface IProps {
    hasAvatar: boolean;
    noAvatarLabel?: string;
    hasAvatarLabel?: string;
    setAvatarUrl(url: string): Promise<unknown>;
}

const MiniAvatarUploader: React.FC<IProps> = ({ hasAvatar, hasAvatarLabel, noAvatarLabel, setAvatarUrl, children }) => {
    const cli = useContext(MatrixClientContext);
    const [busy, setBusy] = useState(false);
    const [hover, setHover] = useState(false);
    const [show, setShow] = useState(false);

    useTimeout(() => {
        setShow(true);
    }, 3000); // show after 3 seconds
    useTimeout(() => {
        setShow(false);
    }, 13000); // hide after being shown for 10 seconds

    const uploadRef = useRef<HTMLInputElement>();

    const label = (hasAvatar || busy) ? hasAvatarLabel : noAvatarLabel;

    const { room } = useContext(RoomContext);
    const canSetAvatar = room?.currentState.maySendStateEvent(EventType.RoomAvatar, cli.getUserId());
    if (!canSetAvatar) return <React.Fragment>{ children }</React.Fragment>;

    const visible = !!label && (hover || show);
    return <React.Fragment>
        <input
            type="file"
            ref={uploadRef}
            className="mx_MiniAvatarUploader_input"
            onChange={async (ev) => {
                if (!ev.target.files?.length) return;
                setBusy(true);
                Analytics.trackEvent("mini_avatar", "upload");
                CountlyAnalytics.instance.track("mini_avatar_upload");
                const file = ev.target.files[0];
                const uri = await cli.uploadContent(file);
                await setAvatarUrl(uri);
                setBusy(false);
            }}
            accept="image/*"
        />

        <AccessibleButton
            className={classNames("mx_MiniAvatarUploader", {
                mx_MiniAvatarUploader_busy: busy,
                mx_MiniAvatarUploader_hasAvatar: hasAvatar,
            })}
            disabled={busy}
            onClick={() => {
                uploadRef.current.click();
            }}
            onMouseOver={() => setHover(true)}
            onMouseLeave={() => setHover(false)}
        >
            { children }

            <div className="mx_MiniAvatarUploader_indicator">
                { busy ?
                    <Spinner w={20} h={20} /> :
                    <div className="mx_MiniAvatarUploader_cameraIcon" /> }
            </div>

            <div className={classNames("mx_Tooltip", {
                "mx_Tooltip_visible": visible,
                "mx_Tooltip_invisible": !visible,
            })}>
                <div className="mx_Tooltip_chevron" />
                { label }
            </div>
        </AccessibleButton>
    </React.Fragment>;
};

export default MiniAvatarUploader;
