import dis from '../../dispatcher/dispatcher';
import SettingsStore from '../SettingsStore';
import IWatcher from "./Watcher";
import { toPx } from '../../utils/units';
import { Action } from '../../dispatcher/actions';
import { SettingLevel } from "../SettingLevel";

export class FontWatcher implements IWatcher {
    public static readonly MIN_SIZE = 8;
    public static readonly MAX_SIZE = 15;
    // Externally we tell the user the font is size 15. Internally we use 10.
    public static readonly SIZE_DIFF = 5;

    private dispatcherRef: string;

    constructor() {
        this.dispatcherRef = null;
    }

    public start() {
        this.setRootFontSize(SettingsStore.getValue("baseFontSize"));
        this.setSystemFont({
            useSystemFont: SettingsStore.getValue("useSystemFont"),
            font: SettingsStore.getValue("systemFont"),
        });
        this.dispatcherRef = dis.register(this.onAction);
    }

    public stop() {
        dis.unregister(this.dispatcherRef);
    }

    private onAction = (payload) => {
        if (payload.action === Action.UpdateFontSize) {
            this.setRootFontSize(payload.size);
        } else if (payload.action === Action.UpdateSystemFont) {
            this.setSystemFont(payload);
        }
    };

    private setRootFontSize = (size) => {
        const fontSize = Math.max(Math.min(FontWatcher.MAX_SIZE, size), FontWatcher.MIN_SIZE);

        if (fontSize !== size) {
            SettingsStore.setValue("baseFontSize", null, SettingLevel.DEVICE, fontSize);
        }
        (<HTMLElement>document.querySelector(":root")).style.fontSize = toPx(fontSize);
    };

    private setSystemFont = ({ useSystemFont, font }) => {
        document.body.style.fontFamily = useSystemFont ? font : "";
    };
}
