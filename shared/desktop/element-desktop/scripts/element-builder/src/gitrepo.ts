import * as childProcess from 'child_process';

export default class GitRepo {
    constructor(
        private path: string,
    ) { }

    public fetch(): Promise<string> {
        return this.gitCmd('fetch');
    }

    public clone(...args): Promise<string> {
        return new Promise((resolve, reject) => {
            childProcess.execFile('git', ['clone', ...args], {}, (err, stdout) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(stdout.trim());
                }
            });
        });
    }

    public checkout(...args): Promise<string> {
        return this.gitCmd('checkout', ...args);
    }

    public getHeadRev(): Promise<string> {
        return this.gitCmd('rev-parse', 'HEAD');
    }

    private gitCmd(cmd: string, ...args): Promise<string> {
        return new Promise((resolve, reject) => {
            childProcess.execFile('git', [cmd, ...args], {
                cwd: this.path,
            }, (err, stdout) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(stdout.trim());
                }
            });
        });
    }
}
