import { RoomListStyle } from "../../settings/RoomListStyle";
import SettingsStore from "../../settings/SettingsStore";
import { TagID } from "./models";

const TILE_HEIGHT_PX_COMPACT = 30;
const TILE_HEIGHT_PX_INTERMEDIATE = 44;
const TILE_HEIGHT_PX_ROOMY = 60;

interface ISerializedListLayout {
    numTiles: number;
    showPreviews: boolean;
    collapsed: boolean;
}

export class ListLayout {
    private _n = 0;
    private _previews = true;
    private _collapsed = false;

    constructor(public readonly tagId: TagID) {
        const serialized = localStorage.getItem(this.key);
        if (serialized) {
            // We don't use the setters as they cause writes.
            const parsed = <ISerializedListLayout>JSON.parse(serialized);
            this._n = parsed.numTiles;
            this._previews = parsed.showPreviews;
            this._collapsed = parsed.collapsed;
        }
    }

    public get isCollapsed(): boolean {
        return this._collapsed;
    }

    public set isCollapsed(v: boolean) {
        this._collapsed = v;
        this.save();
    }

    public get showPreviews(): boolean {
        return this._previews;
    }

    public set showPreviews(v: boolean) {
        this._previews = v;
        this.save();
    }

    public get tileHeight(): number {
        const roomListStyle = SettingsStore.getValue("roomListStyle");
        if (roomListStyle == RoomListStyle.Compact) return TILE_HEIGHT_PX_COMPACT;
        if (roomListStyle == RoomListStyle.Intermediate) return TILE_HEIGHT_PX_INTERMEDIATE;
        return TILE_HEIGHT_PX_ROOMY;
    }

    private get key(): string {
        return `mx_sublist_layout_${this.tagId}_boxed`;
    }

    public get visibleTiles(): number {
        if (this._n === 0) return this.defaultVisibleTiles;
        return Math.max(this._n, this.minVisibleTiles);
    }

    public set visibleTiles(v: number) {
        this._n = v;
        this.save();
    }

    public get minVisibleTiles(): number {
        return 1;
    }

    public get defaultVisibleTiles(): number {
        // This number is what "feels right", and mostly subject to design's opinion.
        return 50;
    }

    public tilesWithPadding(n: number, paddingPx: number): number {
        return this.pixelsToTiles(this.tilesToPixelsWithPadding(n, paddingPx));
    }

    public tilesToPixelsWithPadding(n: number, paddingPx: number): number {
        return this.tilesToPixels(n) + paddingPx;
    }

    public tilesToPixels(n: number): number {
        return n * this.tileHeight;
    }

    public pixelsToTiles(px: number): number {
        return px / this.tileHeight;
    }

    public reset() {
        localStorage.removeItem(this.key);
    }

    private save() {
        localStorage.setItem(this.key, JSON.stringify(this.serialize()));
    }

    private serialize(): ISerializedListLayout {
        return {
            numTiles: this.visibleTiles,
            showPreviews: this.showPreviews,
            collapsed: this.isCollapsed,
        };
    }
}
