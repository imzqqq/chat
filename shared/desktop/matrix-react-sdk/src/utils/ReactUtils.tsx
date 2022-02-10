import React from "react";

/**
 * Joins an array into one value with a joiner. E.g. join(["hello", "world"], " ") -> <span>hello world</span>
 * @param array the array of element to join
 * @param joiner the string/JSX.Chat to join with
 * @returns the joined array
 */
export function jsxJoin(array: Array<string | JSX.Chat>, joiner?: string | JSX.Chat): JSX.Chat {
    const newArray = [];
    array.forEach((element, index) => {
        newArray.push(element, (index === array.length - 1) ? null : joiner);
    });
    return (
        <span>{ newArray }</span>
    );
}
