import { useState } from "react";

// Hook to simplify managing state of arrays of a common type
export const useStateArray = <T>(initialSize: number, initialState: T | T[]): [T[], (i: number, v: T) => void] => {
    const [data, setData] = useState<T[]>(() => {
        return Array.isArray(initialState) ? initialState : new Array(initialSize).fill(initialState);
    });
    return [data, (index: number, value: T) => setData(data => {
        const copy = [...data];
        copy[index] = value;
        return copy;
    })];
};
