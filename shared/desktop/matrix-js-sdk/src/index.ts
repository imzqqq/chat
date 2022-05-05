import * as matrixcs from "./matrix";
import * as utils from "./utils";
import { logger } from './logger';
import request from "request";

matrixcs.request(request);

try {
    // eslint-disable-next-line @typescript-eslint/no-var-requires
    const crypto = require('crypto');
    utils.setCrypto(crypto);
} catch (err) {
    logger.log('nodejs was compiled without crypto support');
}

export * from "./matrix";
export default matrixcs;
