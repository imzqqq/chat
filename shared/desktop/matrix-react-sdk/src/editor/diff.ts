export interface IDiff {
    removed?: string;
    added?: string;
    at?: number;
}

function firstDiff(a: string, b: string): number {
    const compareLen = Math.min(a.length, b.length);
    for (let i = 0; i < compareLen; ++i) {
        if (a[i] !== b[i]) {
            return i;
        }
    }
    return compareLen;
}

function diffStringsAtEnd(oldStr: string, newStr: string): IDiff {
    const len = Math.min(oldStr.length, newStr.length);
    const startInCommon = oldStr.substr(0, len) === newStr.substr(0, len);
    if (startInCommon && oldStr.length > newStr.length) {
        return { removed: oldStr.substr(len), at: len };
    } else if (startInCommon && oldStr.length < newStr.length) {
        return { added: newStr.substr(len), at: len };
    } else {
        const commonStartLen = firstDiff(oldStr, newStr);
        return {
            removed: oldStr.substr(commonStartLen),
            added: newStr.substr(commonStartLen),
            at: commonStartLen,
        };
    }
}

// assumes only characters have been deleted at one location in the string, and none added
export function diffDeletion(oldStr: string, newStr: string): IDiff {
    if (oldStr === newStr) {
        return {};
    }
    const firstDiffIdx = firstDiff(oldStr, newStr);
    const amount = oldStr.length - newStr.length;
    return { at: firstDiffIdx, removed: oldStr.substr(firstDiffIdx, amount) };
}

/**
 * Calculates which string was added and removed around the caret position
 * @param {String} oldValue the previous value
 * @param {String} newValue the new value
 * @param {Number} caretPosition the position of the caret after `newValue` was applied.
 * @return {object} an object with `at` as the offset where characters were removed and/or added,
 *                  `added` with the added string (if any), and
 *                  `removed` with the removed string (if any)
 */
export function diffAtCaret(oldValue: string, newValue: string, caretPosition: number): IDiff {
    const diffLen = newValue.length - oldValue.length;
    const caretPositionBeforeInput = caretPosition - diffLen;
    const oldValueBeforeCaret = oldValue.substr(0, caretPositionBeforeInput);
    const newValueBeforeCaret = newValue.substr(0, caretPosition);
    return diffStringsAtEnd(oldValueBeforeCaret, newValueBeforeCaret);
}
