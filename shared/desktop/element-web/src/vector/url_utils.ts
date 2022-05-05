import { QueryDict, decodeParams } from "matrix-js-sdk/src/utils";

// We want to support some name / value pairs in the fragment
// so we're re-using query string like format
//
// returns {location, params}
export function parseQsFromFragment(location: Location) {
    // if we have a fragment, it will start with '#', which we need to drop.
    // (if we don't, this will return '').
    const fragment = location.hash.substring(1);

    // our fragment may contain a query-param-like section. we need to fish
    // this out *before* URI-decoding because the params may contain ? and &
    // characters which are only URI-encoded once.
    const hashparts = fragment.split('?');

    const result = {
        location: decodeURIComponent(hashparts[0]),
        params: <QueryDict>{},
    };

    if (hashparts.length > 1) {
        result.params = decodeParams(hashparts[1]);
    }
    return result;
}

export function parseQs(location: Location): QueryDict {
    return decodeParams(location.search.substring(1));
}
