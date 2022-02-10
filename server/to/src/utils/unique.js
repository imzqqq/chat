export function orderedUnique(array) {
    const copy = [];
    for (let i = 0; i < array.length; ++i) {
        if (i === 0 || array.lastIndexOf(array[i], i - 1) === -1) {
            copy.push(array[i]);
        }
    }
    return copy;
}