import * as childProcess from 'child_process';

import logger from './logger';

export interface IRunner {
    run(cmd: string, ...args: string[]): Promise<void>;
}

export default class Runner implements IRunner {
    private env: NodeJS.ProcessEnv;

    constructor(
        private cwd: string,
        env?: NodeJS.ProcessEnv,
    ) {
        if (env) {
            this.env = Object.assign({}, process.env, env);
        }
    }

    run(cmd: string, ...args: string[]): Promise<void> {
        logger.info([cmd, ...args].join(' '));
        return new Promise((resolve, reject) => {
            const proc = childProcess.spawn(cmd, args, {
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
