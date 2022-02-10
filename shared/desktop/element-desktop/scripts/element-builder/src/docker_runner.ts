import * as childProcess from 'child_process';

import logger from './logger';
import { IRunner } from './runner';

/**
 * Actually this isn't really a Docker runner: all the docker logic is
 * handled by the in-docker script which is passed in, but for now it's
 * probably least confusing to name it after the thing we use it for.
 */
export default class DockerRunner implements IRunner {
    private env: NodeJS.ProcessEnv;

    constructor(
        private cwd: string,
        private wrapper: string,
        env?: NodeJS.ProcessEnv,
    ) {
        if (env) {
            this.env = Object.assign({}, process.env, env);
        }
    }

    run(cmd: string, ...args: string[]): Promise<void> {
        logger.info([cmd, ...args].join(' '));
        return new Promise((resolve, reject) => {
            const proc = childProcess.spawn(this.wrapper, [cmd].concat(...args), {
                stdio: 'inherit',
                cwd: this.cwd,
                env: this.env,
            });
            proc.on('exit', (code) => {
                code ? reject(code) : resolve();
            });
        });
    }
}
