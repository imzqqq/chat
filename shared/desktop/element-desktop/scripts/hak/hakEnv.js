const path = require('path');
const os = require('os');

const nodePreGypVersioning = require('node-pre-gyp/lib/util/versioning');
const { TARGETS, getHost, isHostId } = require('./target');

function getElectronVersion(packageJson) {
    // should we pick the version of an installed electron
    // dependency, and if so, before or after electronVersion?
    if (packageJson.build && packageJson.build.electronVersion) {
        return packageJson.build.electronVersion;
    }
    return null;
}

function getRuntime(packageJson) {
    const electronVersion = getElectronVersion(packageJson);
    return electronVersion ? 'electron' : 'node-webkit';
}

function getRuntimeVersion(packageJson) {
    const electronVersion = getElectronVersion(packageJson);
    if (electronVersion) {
        return electronVersion;
    } else {
        return process.version.substr(1);
    }
}

module.exports = class HakEnv {
    constructor(prefix, packageJson, targetId) {
        let target;
        if (targetId) {
            target = TARGETS[targetId];
        } else {
            target = getHost();
        }

        if (!target) {
            throw new Error(`Unknown target ${targetId}!`);
        }

        Object.assign(this, {
            // what we're targeting
            runtime: getRuntime(packageJson),
            runtimeVersion: getRuntimeVersion(packageJson),
            target,

            // paths
            projectRoot: prefix,
            dotHakDir: path.join(prefix, '.hak'),
        });
    }

    getRuntimeAbi() {
        return nodePreGypVersioning.get_runtime_abi(
            this.runtime,
            this.runtimeVersion,
        );
    }

    // {node_abi}-{platform}-{arch}
    getNodeTriple() {
        return this.getRuntimeAbi() + '-' + this.target.platform + '-' + this.target.arch;
    }

    getTargetId() {
        return this.target.id;
    }

    isWin() {
        return this.target.platform === 'win32';
    }

    isMac() {
        return this.target.platform === 'darwin';
    }

    isLinux() {
        return this.target.platform === 'linux';
    }

    getTargetArch() {
        return this.target.arch;
    }

    isHost() {
        return isHostId(this.target.id);
    }

    makeGypEnv() {
        return Object.assign({}, process.env, {
            npm_config_arch: this.target.arch,
            npm_config_target_arch: this.target.arch,
            npm_config_disturl: 'https://atom.io/download/electron',
            npm_config_runtime: this.runtime,
            npm_config_target: this.runtimeVersion,
            npm_config_build_from_source: true,
            npm_config_devdir: path.join(os.homedir(), ".electron-gyp"),
        });
    }

    getNodeModuleBin(name) {
        return path.join(this.projectRoot, 'node_modules', '.bin', name);
    }
};
