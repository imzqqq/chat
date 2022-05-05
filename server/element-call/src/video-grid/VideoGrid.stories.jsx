import React, { useState } from "react";
import { VideoGrid, useVideoGridLayout } from "./VideoGrid";
import { VideoTile } from "./VideoTile";
import { useMemo } from "react";
import { Button } from "../button";

export default {
  title: "VideoGrid",
  parameters: {
    layout: "fullscreen",
  },
};

export const ParticipantsTest = () => {
  const [layout, setLayout] = useVideoGridLayout(false);
  const [participantCount, setParticipantCount] = useState(1);

  const items = useMemo(
    () =>
      new Array(participantCount).fill(undefined).map((_, i) => ({
        id: (i + 1).toString(),
        focused: false,
        presenter: false,
      })),
    [participantCount]
  );

  return (
    <>
      <div style={{ display: "flex", width: "100vw", height: "32px" }}>
        <Button
          onPress={() =>
            setLayout((layout) =>
              layout === "freedom" ? "spotlight" : "freedom"
            )
          }
        >
          Toggle Layout
        </Button>
        {participantCount < 12 && (
          <Button onPress={() => setParticipantCount((count) => count + 1)}>
            Add Participant
          </Button>
        )}
        {participantCount > 0 && (
          <Button onPress={() => setParticipantCount((count) => count - 1)}>
            Remove Participant
          </Button>
        )}
      </div>
      <div
        style={{
          display: "flex",
          width: "100vw",
          height: "calc(100vh - 32px)",
        }}
      >
        <VideoGrid layout={layout} items={items}>
          {({ item, ...rest }) => (
            <VideoTile
              key={item.id}
              name={`User ${item.id}`}
              showName={items.length > 2 || item.focused}
              disableSpeakingIndicator={items.length < 3}
              {...rest}
            />
          )}
        </VideoGrid>
      </div>
    </>
  );
};

ParticipantsTest.args = {
  layout: "freedom",
  participantCount: 1,
};
