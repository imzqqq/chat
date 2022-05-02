import React, { useCallback, useEffect, useRef, useState } from "react";
import { useDrag, useGesture } from "@use-gesture/react";
import { useSprings } from "@react-spring/web";
import useMeasure from "react-use-measure";
import { ResizeObserver } from "@juggle/resize-observer";
import moveArrItem from "lodash-move";
import styles from "./VideoGrid.module.css";

export function useVideoGridLayout(hasScreenshareFeeds) {
  const layoutRef = useRef("freedom");
  const revertLayoutRef = useRef("freedom");
  const prevHasScreenshareFeeds = useRef(hasScreenshareFeeds);
  const [, forceUpdate] = useState({});

  const setLayout = useCallback((layout) => {
    // Store the user's set layout to revert to after a screenshare is finished
    revertLayoutRef.current = layout;
    layoutRef.current = layout;
    forceUpdate({});
  }, []);

  // Note: We need the returned layout to update synchronously with a change in hasScreenshareFeeds
  // so use refs and avoid useEffect.
  if (prevHasScreenshareFeeds.current !== hasScreenshareFeeds) {
    if (hasScreenshareFeeds) {
      // Automatically switch to spotlight layout when there's a screenshare
      layoutRef.current = "spotlight";
    } else {
      // When the screenshares have ended, revert to the previous layout
      layoutRef.current = revertLayoutRef.current;
    }
  }

  prevHasScreenshareFeeds.current = hasScreenshareFeeds;

  return [layoutRef.current, setLayout];
}

function useIsMounted() {
  const isMountedRef = useRef(false);

  useEffect(() => {
    isMountedRef.current = true;

    return () => {
      isMountedRef.current = false;
    };
  }, []);

  return isMountedRef;
}

function isInside([x, y], targetTile) {
  const left = targetTile.x;
  const top = targetTile.y;
  const bottom = targetTile.y + targetTile.height;
  const right = targetTile.x + targetTile.width;

  if (x < left || x > right || y < top || y > bottom) {
    return false;
  }

  return true;
}

function getTilePositions(
  tileCount,
  presenterTileCount,
  gridWidth,
  gridHeight,
  layout
) {
  if (layout === "freedom") {
    if (tileCount === 2 && presenterTileCount === 0) {
      return getOneOnOneLayoutTilePositions(gridWidth, gridHeight);
    }

    return getFreedomLayoutTilePositions(
      tileCount,
      presenterTileCount,
      gridWidth,
      gridHeight
    );
  } else {
    return getSpotlightLayoutTilePositions(tileCount, gridWidth, gridHeight);
  }
}

function getOneOnOneLayoutTilePositions(gridWidth, gridHeight) {
  const gap = 8;
  const gridAspectRatio = gridWidth / gridHeight;

  const pipWidth = gridAspectRatio < 1 ? 114 : 230;
  const pipHeight = gridAspectRatio < 1 ? 163 : 155;
  const pipGap = gridAspectRatio < 1 ? 12 : 24;

  return [
    {
      x: gridWidth - pipWidth - gap - pipGap,
      y: gridHeight - pipHeight - gap - pipGap,
      width: pipWidth,
      height: pipHeight,
      zIndex: 1,
    },
    {
      x: gap,
      y: gap,
      width: gridWidth - gap * 2,
      height: gridHeight - gap * 2,
      zIndex: 0,
    },
  ];
}

function getSpotlightLayoutTilePositions(tileCount, gridWidth, gridHeight) {
  const gap = 8;
  const tilePositions = [];

  const gridAspectRatio = gridWidth / gridHeight;

  if (gridAspectRatio < 1) {
    // Vertical layout (mobile)
    const spotlightTileHeight =
      tileCount > 1 ? (gridHeight - gap * 3) * (4 / 5) : gridHeight - gap * 2;
    const spectatorTileSize =
      tileCount > 1 ? gridHeight - gap * 3 - spotlightTileHeight : 0;

    for (let i = 0; i < tileCount; i++) {
      if (i === 0) {
        // Spotlight tile
        tilePositions.push({
          x: gap,
          y: gap,
          width: gridWidth - gap * 2,
          height: spotlightTileHeight,
          zIndex: 0,
        });
      } else {
        // Spectator tile
        tilePositions.push({
          x: (gap + spectatorTileSize) * (i - 1) + gap,
          y: spotlightTileHeight + gap * 2,
          width: spectatorTileSize,
          height: spectatorTileSize,
          zIndex: 0,
        });
      }
    }
  } else {
    // Horizontal layout (desktop)
    const spotlightTileWidth =
      tileCount > 1 ? ((gridWidth - gap * 3) * 4) / 5 : gridWidth - gap * 2;
    const spectatorTileWidth =
      tileCount > 1 ? gridWidth - gap * 3 - spotlightTileWidth : 0;
    const spectatorTileHeight = spectatorTileWidth * (9 / 16);

    for (let i = 0; i < tileCount; i++) {
      if (i === 0) {
        tilePositions.push({
          x: gap,
          y: gap,
          width: spotlightTileWidth,
          height: gridHeight - gap * 2,
          zIndex: 0,
        });
      } else {
        tilePositions.push({
          x: gap * 2 + spotlightTileWidth,
          y: (gap + spectatorTileHeight) * (i - 1) + gap,
          width: spectatorTileWidth,
          height: spectatorTileHeight,
          zIndex: 0,
        });
      }
    }
  }

  return tilePositions;
}

function getFreedomLayoutTilePositions(
  tileCount,
  presenterTileCount,
  gridWidth,
  gridHeight
) {
  if (tileCount === 0) {
    return [];
  }

  if (tileCount > 12) {
    console.warn("Over 12 tiles is not currently supported");
  }

  const gap = 8;

  const { layoutDirection, itemGridRatio } = getGridLayout(
    tileCount,
    presenterTileCount,
    gridWidth,
    gridHeight
  );

  let itemGridWidth;
  let itemGridHeight;

  if (layoutDirection === "vertical") {
    itemGridWidth = gridWidth;
    itemGridHeight = Math.round(gridHeight * itemGridRatio);
  } else {
    itemGridWidth = Math.round(gridWidth * itemGridRatio);
    itemGridHeight = gridHeight;
  }

  const itemTileCount = tileCount - presenterTileCount;

  const {
    columnCount: itemColumnCount,
    rowCount: itemRowCount,
    tileAspectRatio: itemTileAspectRatio,
  } = getSubGridLayout(itemTileCount, itemGridWidth, itemGridHeight);

  const itemGridPositions = getSubGridPositions(
    itemTileCount,
    itemColumnCount,
    itemRowCount,
    itemTileAspectRatio,
    itemGridWidth,
    itemGridHeight,
    gap
  );
  const itemGridBounds = getSubGridBoundingBox(itemGridPositions);

  let presenterGridWidth;
  let presenterGridHeight;

  if (presenterTileCount === 0) {
    presenterGridWidth = 0;
    presenterGridHeight = 0;
  } else if (layoutDirection === "vertical") {
    presenterGridWidth = gridWidth;
    presenterGridHeight =
      gridHeight - (itemGridBounds.height + (itemTileCount ? gap * 2 : 0));
  } else {
    presenterGridWidth =
      gridWidth - (itemGridBounds.width + (itemTileCount ? gap * 2 : 0));
    presenterGridHeight = gridHeight;
  }

  const {
    columnCount: presenterColumnCount,
    rowCount: presenterRowCount,
    tileAspectRatio: presenterTileAspectRatio,
  } = getSubGridLayout(
    presenterTileCount,
    presenterGridWidth,
    presenterGridHeight
  );

  const presenterGridPositions = getSubGridPositions(
    presenterTileCount,
    presenterColumnCount,
    presenterRowCount,
    presenterTileAspectRatio,
    presenterGridWidth,
    presenterGridHeight,
    gap
  );

  const tilePositions = [...presenterGridPositions, ...itemGridPositions];

  centerTiles(
    presenterGridPositions,
    presenterGridWidth,
    presenterGridHeight,
    0,
    0
  );

  if (layoutDirection === "vertical") {
    centerTiles(
      itemGridPositions,
      gridWidth,
      gridHeight - presenterGridHeight,
      0,
      presenterGridHeight
    );
  } else {
    centerTiles(
      itemGridPositions,
      gridWidth - presenterGridWidth,
      gridHeight,
      presenterGridWidth,
      0
    );
  }

  return tilePositions;
}

function getSubGridBoundingBox(positions) {
  let left = 0;
  let right = 0;
  let top = 0;
  let bottom = 0;

  for (let i = 0; i < positions.length; i++) {
    const { x, y, width, height } = positions[i];

    if (i === 0) {
      left = x;
      right = x + width;
      top = y;
      bottom = y + height;
    } else {
      if (x < left) {
        left = x;
      }

      if (y < top) {
        top = y;
      }

      if (x + width > right) {
        right = x + width;
      }

      if (y + height > bottom) {
        bottom = y + height;
      }
    }
  }

  return {
    left,
    right,
    top,
    bottom,
    width: right - left,
    height: bottom - top,
  };
}

function isMobileBreakpoint(gridWidth, gridHeight) {
  const gridAspectRatio = gridWidth / gridHeight;
  return gridAspectRatio < 1;
}

function getGridLayout(tileCount, presenterTileCount, gridWidth, gridHeight) {
  let layoutDirection = "horizontal";
  let itemGridRatio = 1;

  if (presenterTileCount === 0) {
    return { itemGridRatio, layoutDirection };
  }

  if (isMobileBreakpoint(gridWidth, gridHeight)) {
    layoutDirection = "vertical";
    itemGridRatio = 1 / 3;
  } else {
    layoutDirection = "horizontal";
    itemGridRatio = 1 / 3;
  }

  return { itemGridRatio, layoutDirection };
}

function centerTiles(positions, gridWidth, gridHeight, offsetLeft, offsetTop) {
  const bounds = getSubGridBoundingBox(positions);

  const leftOffset = Math.round((gridWidth - bounds.width) / 2) + offsetLeft;
  const topOffset = Math.round((gridHeight - bounds.height) / 2) + offsetTop;

  applyTileOffsets(positions, leftOffset, topOffset);

  return positions;
}

function applyTileOffsets(positions, leftOffset, topOffset) {
  for (const position of positions) {
    position.x += leftOffset;
    position.y += topOffset;
  }

  return positions;
}

function getSubGridLayout(tileCount, gridWidth, gridHeight) {
  const gridAspectRatio = gridWidth / gridHeight;

  let columnCount;
  let rowCount;
  let tileAspectRatio = 16 / 9;

  if (gridAspectRatio < 3 / 4) {
    // Phone
    if (tileCount === 1) {
      columnCount = 1;
      rowCount = 1;
      tileAspectRatio = 0;
    } else if (tileCount <= 4) {
      columnCount = 1;
      rowCount = tileCount;
    } else if (tileCount <= 12) {
      columnCount = 2;
      rowCount = Math.ceil(tileCount / columnCount);
      tileAspectRatio = 0;
    } else {
      // Unsupported
      columnCount = 3;
      rowCount = Math.ceil(tileCount / columnCount);
      tileAspectRatio = 1;
    }
  } else if (gridAspectRatio < 1) {
    // Tablet
    if (tileCount === 1) {
      columnCount = 1;
      rowCount = 1;
      tileAspectRatio = 0;
    } else if (tileCount <= 4) {
      columnCount = 1;
      rowCount = tileCount;
    } else if (tileCount <= 12) {
      columnCount = 2;
      rowCount = Math.ceil(tileCount / columnCount);
    } else {
      // Unsupported
      columnCount = 3;
      rowCount = Math.ceil(tileCount / columnCount);
      tileAspectRatio = 1;
    }
  } else if (gridAspectRatio < 17 / 9) {
    // Computer
    if (tileCount === 1) {
      columnCount = 1;
      rowCount = 1;
    } else if (tileCount === 2) {
      columnCount = 2;
      rowCount = 1;
    } else if (tileCount <= 4) {
      columnCount = 2;
      rowCount = 2;
    } else if (tileCount <= 6) {
      columnCount = 3;
      rowCount = 2;
    } else if (tileCount <= 8) {
      columnCount = 4;
      rowCount = 2;
      tileAspectRatio = 1;
    } else if (tileCount <= 12) {
      columnCount = 4;
      rowCount = 3;
      tileAspectRatio = 1;
    } else {
      // Unsupported
      columnCount = 4;
      rowCount = 4;
    }
  } else if (gridAspectRatio <= 32 / 9) {
    // Ultrawide
    if (tileCount === 1) {
      columnCount = 1;
      rowCount = 1;
    } else if (tileCount === 2) {
      columnCount = 2;
      rowCount = 1;
    } else if (tileCount <= 4) {
      columnCount = 2;
      rowCount = 2;
    } else if (tileCount <= 6) {
      columnCount = 3;
      rowCount = 2;
    } else if (tileCount <= 8) {
      columnCount = 4;
      rowCount = 2;
    } else if (tileCount <= 12) {
      columnCount = 4;
      rowCount = 3;
    } else {
      // Unsupported
      columnCount = 4;
      rowCount = 4;
    }
  } else {
    // Super Ultrawide
    if (tileCount <= 6) {
      columnCount = tileCount;
      rowCount = 1;
    } else {
      columnCount = Math.ceil(tileCount / 2);
      rowCount = 2;
    }
  }

  return { columnCount, rowCount, tileAspectRatio };
}

function getSubGridPositions(
  tileCount,
  columnCount,
  rowCount,
  tileAspectRatio,
  gridWidth,
  gridHeight,
  gap
) {
  if (tileCount === 0) {
    return [];
  }

  const newTilePositions = [];

  const boxWidth = Math.round(
    (gridWidth - gap * (columnCount + 1)) / columnCount
  );
  const boxHeight = Math.round((gridHeight - gap * (rowCount + 1)) / rowCount);

  let tileWidth;
  let tileHeight;

  if (tileAspectRatio) {
    const boxAspectRatio = boxWidth / boxHeight;

    if (boxAspectRatio > tileAspectRatio) {
      tileWidth = boxHeight * tileAspectRatio;
      tileHeight = boxHeight;
    } else {
      tileWidth = boxWidth;
      tileHeight = boxWidth / tileAspectRatio;
    }
  } else {
    tileWidth = boxWidth;
    tileHeight = boxHeight;
  }

  for (let i = 0; i < tileCount; i++) {
    const verticalIndex = Math.floor(i / columnCount);
    const top = verticalIndex * gap + verticalIndex * tileHeight;

    let rowItemCount;

    if (verticalIndex + 1 === rowCount && tileCount % columnCount !== 0) {
      rowItemCount = tileCount % columnCount;
    } else {
      rowItemCount = columnCount;
    }

    const horizontalIndex = i % columnCount;

    let centeringPadding = 0;

    if (rowItemCount < columnCount) {
      const subgridWidth = tileWidth * columnCount + (gap * columnCount - 1);
      centeringPadding = Math.round(
        (subgridWidth - (tileWidth * rowItemCount + (gap * rowItemCount - 1))) /
          2
      );
    }

    const left =
      centeringPadding + gap * horizontalIndex + tileWidth * horizontalIndex;

    newTilePositions.push({
      width: tileWidth,
      height: tileHeight,
      x: left,
      y: top,
      zIndex: 0,
    });
  }

  return newTilePositions;
}

function sortTiles(layout, tiles) {
  const is1on1Freedom = layout === "freedom" && tiles.length === 2;

  tiles.sort((a, b) => {
    if (is1on1Freedom && a.item.isLocal !== b.item.isLocal) {
      return (b.item.isLocal ? 1 : 0) - (a.item.isLocal ? 1 : 0);
    } else if (a.focused !== b.focused) {
      return (b.focused ? 1 : 0) - (a.focused ? 1 : 0);
    } else if (a.presenter !== b.presenter) {
      return (b.presenter ? 1 : 0) - (a.presenter ? 1 : 0);
    }

    return 0;
  });
}

export function VideoGrid({
  items,
  layout,
  onFocusTile,
  disableAnimations,
  children,
}) {
  const [{ tiles, tilePositions, scrollPosition }, setTileState] = useState({
    tiles: [],
    tilePositions: [],
    scrollPosition: 0,
  });
  const draggingTileRef = useRef(null);
  const lastTappedRef = useRef({});
  const lastLayoutRef = useRef(layout);
  const isMounted = useIsMounted();

  const [gridRef, gridBounds] = useMeasure({ polyfill: ResizeObserver });

  useEffect(() => {
    setTileState(({ tiles, ...rest }) => {
      const newTiles = [];
      const removedTileKeys = [];

      for (const tile of tiles) {
        let item = items.find((item) => item.id === tile.key);

        let remove = false;

        if (!item) {
          remove = true;
          item = tile.item;
          removedTileKeys.push(tile.key);
        }

        let focused;
        let presenter = false;

        if (layout === "spotlight") {
          focused = item.focused;
          presenter = item.presenter;
        } else {
          focused = layout === lastLayoutRef.current ? tile.focused : false;
        }

        newTiles.push({
          key: item.id,
          item,
          remove,
          focused,
          presenter,
        });
      }

      for (const item of items) {
        const existingTileIndex = newTiles.findIndex(
          ({ key }) => item.id === key
        );

        const existingTile = newTiles[existingTileIndex];

        if (existingTile && !existingTile.remove) {
          continue;
        }

        const newTile = {
          key: item.id,
          item,
          remove: false,
          focused: layout === "spotlight" && item.focused,
          presenter: layout === "spotlight" && item.presenter,
        };

        if (existingTile) {
          // Replace an existing tile
          newTiles.splice(existingTileIndex, 1, newTile);
        } else {
          // Added tiles
          newTiles.push(newTile);
        }
      }

      sortTiles(layout, newTiles);

      if (removedTileKeys.length > 0) {
        setTimeout(() => {
          if (!isMounted.current) {
            return;
          }

          setTileState(({ tiles, ...rest }) => {
            const newTiles = tiles.filter(
              (tile) => !removedTileKeys.includes(tile.key)
            );

            // TODO: When we remove tiles, we reuse the order of the tiles vs calling sort on the
            // items array. This can cause the local feed to display large in the room.
            // To fix this we need to move to using a reducer and sorting the input items

            const presenterTileCount = newTiles.reduce(
              (count, tile) => count + (tile.focused ? 1 : 0),
              0
            );

            return {
              ...rest,
              tiles: newTiles,
              tilePositions: getTilePositions(
                newTiles.length,
                presenterTileCount,
                gridBounds.width,
                gridBounds.height,
                layout
              ),
            };
          });
        }, 250);
      }

      const presenterTileCount = newTiles.reduce(
        (count, tile) => count + (tile.focused ? 1 : 0),
        0
      );

      lastLayoutRef.current = layout;

      return {
        ...rest,
        tiles: newTiles,
        tilePositions: getTilePositions(
          newTiles.length,
          presenterTileCount,
          gridBounds.width,
          gridBounds.height,
          layout
        ),
      };
    });
  }, [items, gridBounds, layout, isMounted]);

  const animate = useCallback(
    (tiles) => (tileIndex) => {
      const tile = tiles[tileIndex];
      const tilePosition = tilePositions[tileIndex];
      const draggingTile = draggingTileRef.current;
      const dragging = draggingTile && tile.key === draggingTile.key;
      const remove = tile.remove;

      if (dragging) {
        return {
          width: tilePosition.width,
          height: tilePosition.height,
          x: draggingTile.offsetX + draggingTile.x,
          y: draggingTile.offsetY + draggingTile.y,
          scale: 1.1,
          opacity: 1,
          zIndex: 2,
          shadow: 15,
          immediate: (key) =>
            disableAnimations ||
            key === "zIndex" ||
            key === "x" ||
            key === "y" ||
            key === "shadow",
          from: {
            shadow: 0,
            scale: 0,
            opacity: 0,
          },
          reset: false,
        };
      } else {
        const isMobile = isMobileBreakpoint(
          gridBounds.width,
          gridBounds.height
        );

        return {
          x:
            tilePosition.x +
            (layout === "spotlight" && tileIndex !== 0 && isMobile
              ? scrollPosition
              : 0),
          y:
            tilePosition.y +
            (layout === "spotlight" && tileIndex !== 0 && !isMobile
              ? scrollPosition
              : 0),
          width: tilePosition.width,
          height: tilePosition.height,
          scale: remove ? 0 : 1,
          opacity: remove ? 0 : 1,
          zIndex: tilePosition.zIndex,
          shadow: 1,
          from: {
            shadow: 1,
            scale: 0,
            opacity: 0,
          },
          reset: false,
          immediate: (key) =>
            disableAnimations || key === "zIndex" || key === "shadow",
        };
      }
    },
    [tilePositions, disableAnimations, scrollPosition, layout, gridBounds]
  );

  const [springs, api] = useSprings(tiles.length, animate(tiles), [
    tilePositions,
    tiles,
    scrollPosition,
  ]);

  const onTap = useCallback(
    (tileKey) => {
      const lastTapped = lastTappedRef.current[tileKey];

      if (!lastTapped || Date.now() - lastTapped > 500) {
        lastTappedRef.current[tileKey] = Date.now();
        return;
      }

      lastTappedRef.current[tileKey] = 0;

      const tile = tiles.find((tile) => tile.key === tileKey);

      if (!tile) {
        return;
      }

      const item = tile.item;

      setTileState((state) => {
        let presenterTileCount = 0;

        let newTiles;

        if (onFocusTile) {
          newTiles = onFocusTile(state.tiles, tile);

          for (const tile of newTiles) {
            if (tile.focused) {
              presenterTileCount++;
            }
          }
        } else {
          newTiles = state.tiles.map((tile) => {
            let newTile = tile;

            if (tile.item === item) {
              newTile = { ...tile, focused: !tile.focused };
            }

            if (newTile.focused) {
              presenterTileCount++;
            }

            return newTile;
          });
        }

        sortTiles(layout, newTiles);

        return {
          ...state,
          tiles: newTiles,
          tilePositions: getTilePositions(
            newTiles.length,
            presenterTileCount,
            gridBounds.width,
            gridBounds.height,
            layout
          ),
        };
      });
    },
    [tiles, gridBounds, onFocusTile, layout]
  );

  const bindTile = useDrag(
    ({ args: [key], active, xy, movement, tap, event }) => {
      event.preventDefault();

      if (tap) {
        onTap(key);
        return;
      }

      if (layout !== "freedom") {
        return;
      }

      if (layout === "freedom" && tiles.length === 2) {
        return;
      }

      const dragTileIndex = tiles.findIndex((tile) => tile.key === key);
      const dragTile = tiles[dragTileIndex];
      const dragTilePosition = tilePositions[dragTileIndex];

      let newTiles = tiles;

      const cursorPosition = [xy[0] - gridBounds.left, xy[1] - gridBounds.top];

      for (
        let hoverTileIndex = 0;
        hoverTileIndex < tiles.length;
        hoverTileIndex++
      ) {
        const hoverTile = tiles[hoverTileIndex];
        const hoverTilePosition = tilePositions[hoverTileIndex];

        if (hoverTile.key === key) {
          continue;
        }

        if (isInside(cursorPosition, hoverTilePosition)) {
          newTiles = moveArrItem(tiles, dragTileIndex, hoverTileIndex);

          newTiles = newTiles.map((tile) => {
            if (tile === hoverTile) {
              return { ...tile, focused: dragTile.focused };
            } else if (tile === dragTile) {
              return { ...tile, focused: hoverTile.focused };
            } else {
              return tile;
            }
          });

          sortTiles(layout, newTiles);

          setTileState((state) => ({ ...state, tiles: newTiles }));
          break;
        }
      }

      if (active) {
        if (!draggingTileRef.current) {
          draggingTileRef.current = {
            key: dragTile.key,
            offsetX: dragTilePosition.x,
            offsetY: dragTilePosition.y,
            x: movement[0],
            y: movement[1],
          };
        } else {
          draggingTileRef.current.x = movement[0];
          draggingTileRef.current.y = movement[1];
        }
      } else {
        draggingTileRef.current = null;
      }

      api.start(animate(newTiles));
    },
    { filterTaps: true, pointer: { buttons: [1] } }
  );

  const onGridGesture = useCallback(
    (e, isWheel) => {
      if (layout !== "spotlight") {
        return;
      }

      const isMobile = isMobileBreakpoint(gridBounds.width, gridBounds.height);
      let movement = e.delta[isMobile ? 0 : 1];

      if (isWheel) {
        movement = -movement;
      }

      let min = 0;

      if (tilePositions.length > 1) {
        const lastTile = tilePositions[tilePositions.length - 1];
        min = isMobile
          ? gridBounds.width - lastTile.x - lastTile.width - 8
          : gridBounds.height - lastTile.y - lastTile.height - 8;
      }

      setTileState((state) => ({
        ...state,
        scrollPosition: Math.min(
          Math.max(movement + state.scrollPosition, min),
          0
        ),
      }));
    },
    [layout, gridBounds, tilePositions]
  );

  const bindGrid = useGesture(
    {
      onWheel: (e) => onGridGesture(e, true),
      onDrag: (e) => onGridGesture(e, false),
    },
    {}
  );

  return (
    <div className={styles.videoGrid} ref={gridRef} {...bindGrid()}>
      {springs.map(({ shadow, ...style }, i) => {
        const tile = tiles[i];
        const tilePosition = tilePositions[i];

        return children({
          ...bindTile(tile.key),
          key: tile.key,
          style: {
            boxShadow: shadow.to(
              (s) => `rgba(0, 0, 0, 0.5) 0px ${s}px ${2 * s}px 0px`
            ),
            ...style,
          },
          width: tilePosition.width,
          height: tilePosition.height,
          item: tile.item,
        });
      })}
    </div>
  );
}

VideoGrid.defaultProps = {
  layout: "freedom",
};
