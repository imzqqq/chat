import { MatrixGlob } from "../utils/MatrixGlob";

// Inspiration largely taken from Mjolnir itself

export const RECOMMENDATION_BAN = "m.ban";
export const RECOMMENDATION_BAN_TYPES = [RECOMMENDATION_BAN, "org.matrix.mjolnir.ban"];

export function recommendationToStable(recommendation: string, unstable = true): string {
    if (RECOMMENDATION_BAN_TYPES.includes(recommendation)) {
        return unstable ? RECOMMENDATION_BAN_TYPES[RECOMMENDATION_BAN_TYPES.length - 1] : RECOMMENDATION_BAN;
    }
    return null;
}

export class ListRule {
    _glob: MatrixGlob;
    _entity: string;
    _action: string;
    _reason: string;
    _kind: string;

    constructor(entity: string, action: string, reason: string, kind: string) {
        this._glob = new MatrixGlob(entity);
        this._entity = entity;
        this._action = recommendationToStable(action, false);
        this._reason = reason;
        this._kind = kind;
    }

    get entity(): string {
        return this._entity;
    }

    get reason(): string {
        return this._reason;
    }

    get kind(): string {
        return this._kind;
    }

    get recommendation(): string {
        return this._action;
    }

    isMatch(entity: string): boolean {
        return this._glob.test(entity);
    }
}
