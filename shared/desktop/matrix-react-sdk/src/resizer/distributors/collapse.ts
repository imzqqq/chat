import FixedDistributor from "./fixed";
import ResizeItem from "../item";
import Resizer, { IConfig } from "../resizer";
import Sizer from "../sizer";

export interface ICollapseConfig extends IConfig {
    toggleSize: number;
    onCollapsed?(collapsed: boolean, id: string, element: HTMLElement): void;
    isItemCollapsed(element: HTMLElement): boolean;
}

class CollapseItem extends ResizeItem<ICollapseConfig> {
    notifyCollapsed(collapsed: boolean) {
        const callback = this.resizer.config.onCollapsed;
        if (callback) {
            callback(collapsed, this.id, this.domNode);
        }
    }

    get isCollapsed() {
        const isItemCollapsed = this.resizer.config.isItemCollapsed;
        return isItemCollapsed(this.domNode);
    }
}

export default class CollapseDistributor extends FixedDistributor<ICollapseConfig, CollapseItem> {
    static createItem(
        resizeHandle: HTMLDivElement,
        resizer: Resizer<ICollapseConfig>,
        sizer: Sizer,
        container?: HTMLElement,
    ): CollapseItem {
        return new CollapseItem(resizeHandle, resizer, sizer, container);
    }

    private readonly toggleSize: number;
    private isCollapsed: boolean;

    constructor(item: CollapseItem) {
        super(item);
        this.toggleSize = item.resizer?.config?.toggleSize;
        this.isCollapsed = item.isCollapsed;
    }

    public resize(newSize: number) {
        const isCollapsedSize = newSize < this.toggleSize;
        if (isCollapsedSize !== this.isCollapsed) {
            this.isCollapsed = isCollapsedSize;
            this.item.notifyCollapsed(isCollapsedSize);
        }
        if (!isCollapsedSize) {
            super.resize(newSize);
        }
    }
}
