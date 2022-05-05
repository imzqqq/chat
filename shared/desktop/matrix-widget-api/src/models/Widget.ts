import { IWidget, IWidgetData, WidgetType } from "..";
import { assertPresent } from "./validation/utils";
import { ITemplateParams, runTemplate } from "..";

/**
 * Represents the barest form of widget.
 */
export class Widget {
    public constructor(private definition: IWidget) {
        if (!this.definition) throw new Error("Definition is required");

        assertPresent(definition, "id");
        assertPresent(definition, "creatorUserId");
        assertPresent(definition, "type");
        assertPresent(definition, "url");
    }

    /**
     * The user ID who created the widget.
     */
    public get creatorUserId(): string {
        return this.definition.creatorUserId;
    }

    /**
     * The type of widget.
     */
    public get type(): WidgetType {
        return this.definition.type;
    }

    /**
     * The ID of the widget.
     */
    public get id(): string {
        return this.definition.id;
    }

    /**
     * The name of the widget, or null if not set.
     */
    public get name(): string | null {
        return this.definition.name || null;
    }

    /**
     * The title for the widget, or null if not set.
     */
    public get title(): string | null {
        return this.rawData.title || null;
    }

    /**
     * The templated URL for the widget.
     */
    public get templateUrl(): string {
        return this.definition.url;
    }

    /**
     * The origin for this widget.
     */
    public get origin(): string {
        return new URL(this.templateUrl).origin;
    }

    /**
     * Whether or not the client should wait for the iframe to load. Defaults
     * to true.
     */
    public get waitForIframeLoad(): boolean {
        if (this.definition.waitForIframeLoad === false) return false;
        if (this.definition.waitForIframeLoad === true) return true;
        return true; // default true
    }

    /**
     * The raw data for the widget. This will always be defined, though
     * may be empty.
     */
    public get rawData(): IWidgetData {
        return this.definition.data || {};
    }

    /**
     * Gets a complete widget URL for the client to render.
     * @param {ITemplateParams} params The template parameters.
     * @returns {string} A templated URL.
     */
    public getCompleteUrl(params: ITemplateParams): string {
        return runTemplate(this.templateUrl, this.definition, params);
    }
}
