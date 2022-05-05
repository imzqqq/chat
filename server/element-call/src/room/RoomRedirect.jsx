import React, { useEffect } from "react";
import { useLocation, useHistory } from "react-router-dom";
import { defaultHomeserverHost } from "../matrix-utils";
import { LoadingView } from "../FullScreenView";

export function RoomRedirect() {
  const { pathname } = useLocation();
  const history = useHistory();

  useEffect(() => {
    let roomId = pathname;

    if (pathname.startsWith("/")) {
      roomId = roomId.substring(1, roomId.length);
    }

    if (!roomId.startsWith("#") && !roomId.startsWith("!")) {
      roomId = `#${roomId}:${defaultHomeserverHost}`;
    }

    history.replace(`/room/${roomId.toLowerCase()}`);
  }, [pathname, history]);

  return <LoadingView />;
}
