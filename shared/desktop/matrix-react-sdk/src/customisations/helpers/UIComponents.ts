import { UIComponent } from "../../settings/UIFeature";
import { ComponentVisibilityCustomisations } from "../ComponentVisibility";

export function shouldShowComponent(component: UIComponent): boolean {
    return ComponentVisibilityCustomisations.shouldShowComponent?.(component) ?? true;
}
