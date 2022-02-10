const childProcess = require('child_process');

module.exports = async function(hakEnv, moduleInfo) {
    const tools = [['python', '--version']]; // node-gyp uses python for reasons beyond comprehension

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
};
