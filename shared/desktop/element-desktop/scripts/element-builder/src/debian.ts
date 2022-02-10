import * as childProcess from 'child_process';
import { promises as fsProm } from 'fs';
import * as path from 'path';

import logger from './logger';

async function getRepoTargets(repoDir: string): Promise<string[]> {
    const confDistributions = await fsProm.readFile(path.join(repoDir, 'conf', 'distributions'), 'utf8');
    const ret = [];
    for (const line of confDistributions.split('\n')) {
        if (line.startsWith('Codename')) {
            ret.push(line.split(': ')[1]);
        }
    }
    return ret;
}

export async function setDebVersion(ver: string, templateFile: string, outFile: string): Promise<void> {
    // Create a debian package control file with the version.
    // We use a custom control file so we need to do this ourselves
    let contents = await fsProm.readFile(templateFile, 'utf8');
    contents += 'Version: ' + ver + "\n";
    await fsProm.writeFile(outFile, contents);

    logger.info("Version set to " + ver);
}

export async function addDeb(debDir: string, deb: string): Promise<void> {
    const targets = await getRepoTargets(debDir);
    logger.info("Adding " + deb + " for " + targets.join(', ') + "...");
    for (const target of targets) {
        await new Promise<void>((resolve, reject) => {
            const proc = childProcess.spawn('reprepro', [
                'includedeb', target, deb,
            ], {
                stdio: 'inherit',
                cwd: debDir,
            });
            proc.on('exit', code => {
                code ? reject(code) : resolve();
            });
        });
    }
}
