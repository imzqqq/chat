import { logger } from '../src/logger';
import * as utils from "../src/utils";

// try to load the olm library.
try {
    global.Olm = require('@matrix-org/olm');
    logger.log('loaded libolm');
} catch (e) {
    logger.warn("unable to run crypto tests: libolm not available");
}

// also try to set node crypto
try {
    const crypto = require('crypto');
    utils.setCrypto(crypto);
} catch (err) {
    logger.log('nodejs was compiled without crypto support: some tests will fail');
}
