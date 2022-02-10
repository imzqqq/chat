import Sizer from "../sizer";
import FixedDistributor from "./fixed";
import { IConfig } from "../resizer";

class PercentageSizer extends Sizer {
    public start(item: HTMLElement) {
        if (this.vertical) {
            item.style.minHeight = null;
        } else {
            item.style.minWidth = null;
        }
    }

    public finish(item: HTMLElement) {
        const parent = item.offsetParent as HTMLElement;
        if (!parent) return;
        if (this.vertical) {
            const p = ((item.offsetHeight / parent.offsetHeight) * 100).toFixed(2) + "%";
            item.style.minHeight = p;
            item.style.height = p;
        } else {
            const p = ((item.offsetWidth / parent.offsetWidth) * 100).toFixed(2) + "%";
            item.style.minWidth = p;
            item.style.width = p;
        }
    }
}

export default class PercentageDistributor extends FixedDistributor<IConfig> {
    static createSizer(containerElement: HTMLElement, vertical: boolean, reverse: boolean) {
        return new PercentageSizer(containerElement, vertical, reverse);
    }
}
