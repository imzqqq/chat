const childProcess = require('child_process');
const fsProm = require('fs').promises;

module.exports = async function(hakEnv, moduleInfo) {
    // of course tcl doesn't have a --version
    if (!hakEnv.isLinux()) {
        await new Promise((resolve, reject) => {
            const proc = childProcess.spawn('tclsh', [], {
                stdio: ['pipe', 'ignore', 'ignore'],
            });
            proc.on('exit', (code) => {
                if (code !== 0) {
                    reject("Can't find tclsh - have you installed TCL?");
                } else {
                    resolve();
                }
            });
            proc.stdin.end();
        });
    }

    const tools = [
        ['rustc', '--version'],
        ['python', '--version'], // node-gyp uses python for reasons beyond comprehension
    ];
    if (hakEnv.isWin()) {
        tools.push(['perl', '--version']); // for openssl configure
        tools.push(['patch', '--version']); // to patch sqlcipher Makefile.msc
        tools.push(['nmake', '/?']);
    } else {
        tools.push(['make', '--version']);
    }

    for (const tool of tools) {
        await new Promise((resolve, reject) => {
            const proc = childProcess.spawn(tool[0], tool.slice(1), {
                stdio: ['ignore'],
            });
            proc.on('exit', (code) => {
                if (code !== 0) {
                    reject("Can't find " + tool);
                } else {
                    resolve();
                }
            });
        });
    }

    // Ensure Rust target exists (nb. we avoid depending on rustup)
    await new Promise((resolve, reject) => {
        const rustc = childProcess.execFile('rustc', [
            '--target', hakEnv.getTargetId(), '-o', 'tmp', '-',
        ], (err, out) => {
            if (err) {
                reject(
                    "rustc can't build for target " + hakEnv.getTargetId() +
                    ": ensure target is installed via `rustup target add " + hakEnv.getTargetId() + "` " +
                    "or your package manager if not using `rustup`",
                );
            }
            fsProm.unlink('tmp').then(resolve);
        });
        rustc.stdin.write('fn main() {}');
        rustc.stdin.end();
    });
};
