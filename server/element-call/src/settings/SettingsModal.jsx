import React from "react";
import { Modal } from "../Modal";
import styles from "./SettingsModal.module.css";
import { TabContainer, TabItem } from "../tabs/Tabs";
import { ReactComponent as AudioIcon } from "../icons/Audio.svg";
import { ReactComponent as VideoIcon } from "../icons/Video.svg";
import { ReactComponent as DeveloperIcon } from "../icons/Developer.svg";
import { SelectInput } from "../input/SelectInput";
import { Item } from "@react-stately/collections";
import { useMediaHandler } from "./useMediaHandler";
import { FieldRow, InputField } from "../input/Input";
import { Button } from "../button";
import { useDownloadDebugLog } from "./submit-rageshake";
import { Body } from "../typography/Typography";

export function SettingsModal({ setShowInspector, showInspector, ...rest }) {
  const {
    audioInput,
    audioInputs,
    setAudioInput,
    videoInput,
    videoInputs,
    setVideoInput,
    audioOutput,
    audioOutputs,
    setAudioOutput,
  } = useMediaHandler();

  const downloadDebugLog = useDownloadDebugLog();

  return (
    <Modal
      title="Settings"
      isDismissable
      mobileFullScreen
      className={styles.settingsModal}
      {...rest}
    >
      <TabContainer className={styles.tabContainer}>
        <TabItem
          title={
            <>
              <AudioIcon width={16} height={16} />
              <span>Audio</span>
            </>
          }
        >
          <SelectInput
            label="Microphone"
            selectedKey={audioInput}
            onSelectionChange={setAudioInput}
          >
            {audioInputs.map(({ deviceId, label }) => (
              <Item key={deviceId}>{label}</Item>
            ))}
          </SelectInput>
          {audioOutputs.length > 0 && (
            <SelectInput
              label="Speaker"
              selectedKey={audioOutput}
              onSelectionChange={setAudioOutput}
            >
              {audioOutputs.map(({ deviceId, label }) => (
                <Item key={deviceId}>{label}</Item>
              ))}
            </SelectInput>
          )}
        </TabItem>
        <TabItem
          title={
            <>
              <VideoIcon width={16} height={16} />
              <span>Video</span>
            </>
          }
        >
          <SelectInput
            label="Webcam"
            selectedKey={videoInput}
            onSelectionChange={setVideoInput}
          >
            {videoInputs.map(({ deviceId, label }) => (
              <Item key={deviceId}>{label}</Item>
            ))}
          </SelectInput>
        </TabItem>
        <TabItem
          title={
            <>
              <DeveloperIcon width={16} height={16} />
              <span>Developer</span>
            </>
          }
        >
          <FieldRow>
            <Body className={styles.fieldRowText}>
              Version: {import.meta.env.VITE_APP_VERSION || "dev"}
            </Body>
          </FieldRow>
          <FieldRow>
            <InputField
              id="showInspector"
              name="inspector"
              label="Show Call Inspector"
              type="checkbox"
              checked={showInspector}
              onChange={(e) => setShowInspector(e.target.checked)}
            />
          </FieldRow>
          <FieldRow>
            <Button onPress={downloadDebugLog}>Download Debug Logs</Button>
          </FieldRow>
        </TabItem>
      </TabContainer>
    </Modal>
  );
}
