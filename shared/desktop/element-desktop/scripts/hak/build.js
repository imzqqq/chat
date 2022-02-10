async function build(hakEnv, moduleInfo) {
    await moduleInfo.scripts.build(hakEnv, moduleInfo);
}

module.exports = build;
