import React, { JSXElementConstructor } from "react";

// Based on https://stackoverflow.com/a/53229857/3532235
export type Without<T, U> = {[P in Exclude<keyof T, keyof U>]?: never};
export type XOR<T, U> = (T | U) extends object ? (Without<T, U> & U) | (Without<U, T> & T) : T | U;
export type Writeable<T> = { -readonly [P in keyof T]: T[P] };

export type ComponentClass = keyof JSX.IntrinsicElements | JSXElementConstructor<any>;
export type ReactAnyComponent = React.Component | React.ExoticComponent;
