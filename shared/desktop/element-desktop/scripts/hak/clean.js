const path = require('path');

const rimraf = require('rimraf');

async function clean(hakEnv, moduleInfo) {
    await new Promise((resolve, reject) => {
        rimraf(moduleInfo.moduleDotHakDir, (err) => {
            if (err) {
                reject(err);
            } else {
                resolve();
            }
        });
    });

    await new Promise((resolve, reject) => {
        rimraf(path.join(hakEnv.dotHakDir, 'links', moduleInfo.name), (err) => {
            if (err) {
                reject(err);
            } else {
                resolve();
            }
        });
    });

    await new Promise((resolve, reject) => {
        rimraf(path.join(hakEnv.projectRoot, 'node_modules', moduleInfo.name), (err) => {
            if (err) {
                reject(err);
            } else {
                resolve();
            }
        });
    });
}

module.exports = clean;
