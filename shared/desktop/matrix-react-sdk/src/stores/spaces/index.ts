import { Room } from "matrix-js-sdk/src/models/room";
import { IHierarchyRoom } from "matrix-js-sdk/src/@types/spaces";

// The consts & types are moved out here to prevent cyclical imports

export const UPDATE_TOP_LEVEL_SPACES = Symbol("top-level-spaces");
export const UPDATE_INVITED_SPACES = Symbol("invited-spaces");
export const UPDATE_SELECTED_SPACE = Symbol("selected-space");
export const UPDATE_HOME_BEHAVIOUR = Symbol("home-behaviour");
export const UPDATE_SUGGESTED_ROOMS = Symbol("suggested-rooms");
// Space Key will be emitted when a Space's children change

export enum MetaSpace {
    Home = "home-space",
    Favourites = "favourites-space",
    People = "people-space",
    Orphans = "orphans-space",
}

export type SpaceKey = MetaSpace | Room["roomId"];

export interface ISuggestedRoom extends IHierarchyRoom {
    viaServers: string[];
}
