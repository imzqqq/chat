/**
 * @module logger
 */

import log, { Logger } from "loglevel";

// This is to demonstrate, that you can use any namespace you want.
// Namespaces allow you to turn on/off the logging for specific parts of the
// application.
// An idea would be to control this via an environment variable (on Node.js).
// See https://www.npmjs.com/package/debug to see how this could be implemented
// Part of #332 is introducing a logging library in the first place.
const DEFAULT_NAMESPACE = "matrix";

// because rageshakes in react-sdk hijack the console log, also at module load time,
// initializing the logger here races with the initialization of rageshakes.
// to avoid the issue, we override the methodFactory of loglevel that binds to the
// console methods at initialization time by a factory that looks up the console methods
// when logging so we always get the current value of console methods.
log.methodFactory = function(methodName, logLevel, loggerName) {
    return function(...args) {
        /* eslint-disable @typescript-eslint/no-invalid-this */
        if (this.prefix) {
            args.unshift(this.prefix);
        }
        /* eslint-enable @typescript-eslint/no-invalid-this */
        const supportedByConsole = methodName === "error" ||
            methodName === "warn" ||
            methodName === "trace" ||
            methodName === "info";
        /* eslint-disable no-console */
        if (supportedByConsole) {
            return console[methodName](...args);
        } else {
            return console.log(...args);
        }
        /* eslint-enable no-console */
    };
};

/**
 * Drop-in replacement for <code>console</code> using {@link https://www.npmjs.com/package/loglevel|loglevel}.
 * Can be tailored down to specific use cases if needed.
 */
export const logger: PrefixedLogger = log.getLogger(DEFAULT_NAMESPACE);
logger.setLevel(log.levels.DEBUG, false);

export interface PrefixedLogger extends Logger {
    withPrefix?: (prefix: string) => PrefixedLogger;
    prefix?: string;
}

function extendLogger(logger: PrefixedLogger) {
    logger.withPrefix = function(prefix: string): PrefixedLogger {
        const existingPrefix = this.prefix || "";
        return getPrefixedLogger(existingPrefix + prefix);
    };
}

extendLogger(logger);

function getPrefixedLogger(prefix): PrefixedLogger {
    const prefixLogger: PrefixedLogger = log.getLogger(`${DEFAULT_NAMESPACE}-${prefix}`);
    if (prefixLogger.prefix !== prefix) {
        // Only do this setup work the first time through, as loggers are saved by name.
        extendLogger(prefixLogger);
        prefixLogger.prefix = prefix;
        prefixLogger.setLevel(log.levels.DEBUG, false);
    }
    return prefixLogger;
}
