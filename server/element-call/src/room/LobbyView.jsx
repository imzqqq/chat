import React, { useEffect, useRef } from "react";
import styles from "./LobbyView.module.css";
import { Button, CopyButton, MicButton, VideoButton } from "../button";
import { Header, LeftNav, RightNav, RoomHeaderInfo } from "../Header";
import { GroupCallState } from "matrix-js-sdk/src/webrtc/groupCall";
import { useCallFeed } from "../video-grid/useCallFeed";
import { useMediaStream } from "../video-grid/useMediaStream";
import { getRoomUrl } from "../matrix-utils";
import { OverflowMenu } from "./OverflowMenu";
import { UserMenuContainer } from "../UserMenuContainer";
import { Body, Link } from "../typography/Typography";
import { Avatar } from "../Avatar";
import { useProfile } from "../profile/useProfile";
import useMeasure from "react-use-measure";
import { ResizeObserver } from "@juggle/resize-observer";
import { useLocationNavigation } from "../useLocationNavigation";
import { useMediaHandler } from "../settings/useMediaHandler";

export function LobbyView({
  client,
  roomName,
  state,
  onInitLocalCallFeed,
  onEnter,
  localCallFeed,
  microphoneMuted,
  localVideoMuted,
  toggleLocalVideoMuted,
  toggleMicrophoneMuted,
  setShowInspector,
  showInspector,
  roomId,
}) {
  const { stream } = useCallFeed(localCallFeed);
  const { audioOutput } = useMediaHandler();
  const videoRef = useMediaStream(stream, audioOutput, true);
  const { displayName, avatarUrl } = useProfile(client);
  const [previewRef, previewBounds] = useMeasure({ polyfill: ResizeObserver });
  const avatarSize = (previewBounds.height - 66) / 2;

  useEffect(() => {
    onInitLocalCallFeed();
  }, [onInitLocalCallFeed]);

  useLocationNavigation(state === GroupCallState.InitializingLocalCallFeed);

  const joinCallButtonRef = useRef();

  useEffect(() => {
    if (state === GroupCallState.LocalCallFeedInitialized) {
      joinCallButtonRef.current.focus();
    }
  }, [state]);

  return (
    <div className={styles.room}>
      <Header>
        <LeftNav>
          <RoomHeaderInfo roomName={roomName} />
        </LeftNav>
        <RightNav>
          <UserMenuContainer />
        </RightNav>
      </Header>
      <div className={styles.joinRoom}>
        <div className={styles.joinRoomContent}>
          <div className={styles.preview} ref={previewRef}>
            <video ref={videoRef} muted playsInline disablePictureInPicture />
            {state === GroupCallState.LocalCallFeedUninitialized && (
              <Body fontWeight="semiBold" className={styles.webcamPermissions}>
                Webcam/microphone permissions needed to join the call.
              </Body>
            )}
            {state === GroupCallState.InitializingLocalCallFeed && (
              <Body fontWeight="semiBold" className={styles.webcamPermissions}>
                Accept webcam/microphone permissions to join the call.
              </Body>
            )}
            {state === GroupCallState.LocalCallFeedInitialized && (
              <>
                {localVideoMuted && (
                  <div className={styles.avatarContainer}>
                    <Avatar
                      style={{
                        width: avatarSize,
                        height: avatarSize,
                        borderRadius: avatarSize,
                        fontSize: Math.round(avatarSize / 2),
                      }}
                      src={avatarUrl}
                      fallback={displayName.slice(0, 1).toUpperCase()}
                    />
                  </div>
                )}
                <div className={styles.previewButtons}>
                  <MicButton
                    muted={microphoneMuted}
                    onPress={toggleMicrophoneMuted}
                  />
                  <VideoButton
                    muted={localVideoMuted}
                    onPress={toggleLocalVideoMuted}
                  />
                  <OverflowMenu
                    roomId={roomId}
                    setShowInspector={setShowInspector}
                    showInspector={showInspector}
                    client={client}
                  />
                </div>
              </>
            )}
          </div>
          <Button
            ref={joinCallButtonRef}
            className={styles.copyButton}
            size="lg"
            disabled={state !== GroupCallState.LocalCallFeedInitialized}
            onPress={onEnter}
          >
            Join call now
          </Button>
          <Body>Or</Body>
          <CopyButton
            variant="secondaryCopy"
            value={getRoomUrl(roomId)}
            className={styles.copyButton}
            copiedMessage="Call link copied"
          >
            Copy call link and join later
          </CopyButton>
        </div>
        <Body className={styles.joinRoomFooter}>
          <Link color="primary" to="/">
            Take me Home
          </Link>
        </Body>
      </div>
    </div>
  );
}
