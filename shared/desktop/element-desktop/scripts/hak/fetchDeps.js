const mkdirp = require('mkdirp');

async function fetchDeps(hakEnv, moduleInfo) {
    await mkdirp(moduleInfo.moduleDotHakDir);
    if (moduleInfo.scripts.fetchDeps) {
        await moduleInfo.scripts.fetchDeps(hakEnv, moduleInfo);
    }
}

module.exports = fetchDeps;
