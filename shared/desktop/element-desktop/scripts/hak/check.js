async function check(hakEnv, moduleInfo) {
    if (moduleInfo.scripts.check) {
        await moduleInfo.scripts.check(hakEnv, moduleInfo);
    }
}

module.exports = check;
