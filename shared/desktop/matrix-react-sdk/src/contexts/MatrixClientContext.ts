import { createContext } from "react";
import { MatrixClient } from "matrix-js-sdk/src/client";

const MatrixClientContext = createContext<MatrixClient>(undefined);
MatrixClientContext.displayName = "MatrixClientContext";
export default MatrixClientContext;
