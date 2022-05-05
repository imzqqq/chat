import { ImportanceAlgorithm } from "./ImportanceAlgorithm";
import { ListAlgorithm, SortAlgorithm } from "../models";
import { NaturalAlgorithm } from "./NaturalAlgorithm";
import { TagID } from "../../models";
import { OrderingAlgorithm } from "./OrderingAlgorithm";

interface AlgorithmFactory {
    (tagId: TagID, initialSortingAlgorithm: SortAlgorithm): OrderingAlgorithm;
}

const ALGORITHM_FACTORIES: { [algorithm in ListAlgorithm]: AlgorithmFactory } = {
    [ListAlgorithm.Natural]: (tagId, initSort) => new NaturalAlgorithm(tagId, initSort),
    [ListAlgorithm.Importance]: (tagId, initSort) => new ImportanceAlgorithm(tagId, initSort),
};

/**
 * Gets an instance of the defined algorithm
 * @param {ListAlgorithm} algorithm The algorithm to get an instance of.
 * @param {TagID} tagId The tag the algorithm is for.
 * @param {SortAlgorithm} initSort The initial sorting algorithm for the ordering algorithm.
 * @returns {Algorithm} The algorithm instance.
 */
export function getListAlgorithmInstance(
    algorithm: ListAlgorithm,
    tagId: TagID,
    initSort: SortAlgorithm,
): OrderingAlgorithm {
    if (!ALGORITHM_FACTORIES[algorithm]) {
        throw new Error(`${algorithm} is not a known algorithm`);
    }

    return ALGORITHM_FACTORIES[algorithm](tagId, initSort);
}
