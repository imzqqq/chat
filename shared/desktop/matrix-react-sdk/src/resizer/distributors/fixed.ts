import ResizeItem from "../item";
import Sizer from "../sizer";
import Resizer, { IConfig } from "../resizer";

/**
distributors translate a moving cursor into
CSS/DOM changes by calling the sizer

they have two methods:
    `resize` receives then new item size
    `resizeFromContainerOffset` receives resize handle location
        within the container bounding box. For internal use.
        This method usually ends up calling `resize` once the start offset is subtracted.
*/
export default class FixedDistributor<C extends IConfig, I extends ResizeItem<any> = ResizeItem<C>> {
    static createItem(resizeHandle: HTMLDivElement, resizer: Resizer, sizer: Sizer): ResizeItem {
        return new ResizeItem(resizeHandle, resizer, sizer);
    }

    static createSizer(containerElement: HTMLElement, vertical: boolean, reverse: boolean): Sizer {
        return new Sizer(containerElement, vertical, reverse);
    }

    private readonly beforeOffset: number;

    constructor(public readonly item: I) {
        this.beforeOffset = item.offset();
    }

    public get size() {
        return this.item.getSize();
    }

    public set size(size: string) {
        this.item.setRawSize(size);
    }

    public resize(size: number) {
        this.item.setSize(size);
    }

    public resizeFromContainerOffset(offset: number) {
        this.resize(offset - this.beforeOffset);
    }

    public start() {
        this.item.start();
    }

    public finish() {
        this.item.finish();
    }
}
