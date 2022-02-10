const path = require('path');
const childProcess = require('child_process');

module.exports = async function(hakEnv, moduleInfo) {
    await buildKeytar(hakEnv, moduleInfo);
};

async function buildKeytar(hakEnv, moduleInfo) {
    const env = hakEnv.makeGypEnv();

    console.log("Running yarn with env", env);
    await new Promise((resolve, reject) => {
        const proc = childProcess.spawn(
            path.join(moduleInfo.nodeModuleBinDir, 'node-gyp' + (hakEnv.isWin() ? '.cmd' : '')),
            ['rebuild'],
            {
                cwd: moduleInfo.moduleBuildDir,
                env,
                stdio: 'inherit',
            },
        );
        proc.on('exit', (code) => {
            code ? reject(code) : resolve();
        });
    });
}
