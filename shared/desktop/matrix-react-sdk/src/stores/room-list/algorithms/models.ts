import { TagID } from "../models";
import { Room } from "matrix-js-sdk/src/models/room";
import { OrderingAlgorithm } from "./list-ordering/OrderingAlgorithm";

export enum SortAlgorithm {
    Manual = "MANUAL",
    Alphabetic = "ALPHABETIC",
    Recent = "RECENT",
}

export enum ListAlgorithm {
    // Orders Red > Grey > Bold > Idle
    Importance = "IMPORTANCE",

    // Orders however the SortAlgorithm decides
    Natural = "NATURAL",
}

export interface ITagSortingMap {
    // @ts-ignore - TypeScript really wants this to be [tagId: string] but we know better.
    [tagId: TagID]: SortAlgorithm;
}

export interface IListOrderingMap {
    // @ts-ignore - TypeScript really wants this to be [tagId: string] but we know better.
    [tagId: TagID]: ListAlgorithm;
}

export interface IOrderingAlgorithmMap {
    // @ts-ignore - TypeScript really wants this to be [tagId: string] but we know better.
    [tagId: TagID]: OrderingAlgorithm;
}

export interface ITagMap {
    // @ts-ignore - TypeScript really wants this to be [tagId: string] but we know better.
    [tagId: TagID]: Room[];
}
