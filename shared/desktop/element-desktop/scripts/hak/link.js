const path = require('path');
const os = require('os');
const fsProm = require('fs').promises;
const childProcess = require('child_process');

async function link(hakEnv, moduleInfo) {
    const yarnrc = path.join(hakEnv.projectRoot, '.yarnrc');
    // this is fairly terrible but it's reasonably clunky to either parse a yarnrc
    // properly or get yarn to do it, so this will probably suffice for now.
    // We just check to see if there is a local .yarnrc at all, and assume that
    // if you've put one there yourself, you probably know what you're doing and
    // we won't meddle with it.
    // Also we do this for each module which is unnecessary, but meh.
    try {
        await fsProm.stat(yarnrc);
    } catch (e) {
        await fsProm.writeFile(
            yarnrc,
            // XXX: 1. This must be absolute, as yarn will resolve link directories
            // relative to the closest project root, which means when we run it
            // in the dependency project, it will put the link directory in its
            // own project folder rather than the main project.
            // 2. The parser gets very confused by strings with colons in them
            // (ie. Windows absolute paths) but strings in quotes get parsed as
            // JSON so need to be valid JSON encoded strings (ie. have the
            // backslashes escaped). JSON.stringify will add quotes and escape.
            '--link-folder ' + JSON.stringify(path.join(hakEnv.dotHakDir, 'links')) + os.EOL,
        );
    }

    const yarnCmd = 'yarn' + (hakEnv.isWin() ? '.cmd' : '');

    await new Promise((resolve, reject) => {
        const proc = childProcess.spawn(yarnCmd, ['link'], {
            cwd: moduleInfo.moduleOutDir,
            stdio: 'inherit',
        });
        proc.on('exit', code => {
            code ? reject(code) : resolve();
        });
    });

    await new Promise((resolve, reject) => {
        const proc = childProcess.spawn(yarnCmd, ['link', moduleInfo.name], {
            cwd: hakEnv.projectRoot,
            stdio: 'inherit',
        });
        proc.on('exit', code => {
            code ? reject(code) : resolve();
        });
    });
}

module.exports = link;
