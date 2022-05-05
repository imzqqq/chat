import { useState, useEffect, DependencyList } from 'react';

type Fn<T> = () => Promise<T>;

export const useAsyncMemo = <T>(fn: Fn<T>, deps: DependencyList, initialValue?: T): T => {
    const [value, setValue] = useState<T>(initialValue);
    useEffect(() => {
        let discard = false;
        fn().then(v => {
            if (!discard) {
                setValue(v);
            }
        });
        return () => {
            discard = true;
        };
    }, deps); // eslint-disable-line react-hooks/exhaustive-deps
    return value;
};
