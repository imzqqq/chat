"use strict";

/*
 * THIS FILE IS GENERATED, NOT MEANT FOR EDITING DIRECTLY
 * The original source is `target.ts` in the `element-builder` dir. You can
 * edit it over there, run `yarn build`, and paste the changes here. It is
 * currently assumed this file will rarely change, so a spearate package is not
 * yet warranted.
 */

Object.defineProperty(exports, "__esModule", { value: true });
exports.isHost = exports.isHostId = exports.getHost = exports.ENABLED_TARGETS = exports.TARGETS = void 0;
const aarch64AppleDarwin = {
    id: 'aarch64-apple-darwin',
    platform: 'darwin',
    arch: 'arm64',
};
const i686PcWindowsMsvc = {
    id: 'i686-pc-windows-msvc',
    platform: 'win32',
    arch: 'ia32',
    vcVarsArch: 'x86',
};
const x8664PcWindowsMsvc = {
    id: 'x86_64-pc-windows-msvc',
    platform: 'win32',
    arch: 'x64',
    vcVarsArch: 'amd64',
};
const x8664AppleDarwin = {
    id: 'x86_64-apple-darwin',
    platform: 'darwin',
    arch: 'x64',
};
const x8664UnknownLinuxGnu = {
    id: 'x86_64-unknown-linux-gnu',
    platform: 'linux',
    arch: 'x64',
};
exports.TARGETS = {
    'aarch64-apple-darwin': aarch64AppleDarwin,
    'i686-pc-windows-msvc': i686PcWindowsMsvc,
    'x86_64-pc-windows-msvc': x8664PcWindowsMsvc,
    'x86_64-apple-darwin': x8664AppleDarwin,
    'x86_64-unknown-linux-gnu': x8664UnknownLinuxGnu,
};
// The set of targets we build by default, sorted by increasing complexity so
// that we fail fast when the native host target fails.
exports.ENABLED_TARGETS = [
    exports.TARGETS['x86_64-apple-darwin'],
    exports.TARGETS['aarch64-apple-darwin'],
    exports.TARGETS['x86_64-unknown-linux-gnu'],
    exports.TARGETS['i686-pc-windows-msvc'],
];
function getHost() {
    return Object.values(exports.TARGETS).find(target => (target.platform === process.platform &&
        target.arch === process.arch));
}
exports.getHost = getHost;
function isHostId(id) {
    return getHost()?.id === id;
}
exports.isHostId = isHostId;
function isHost(target) {
    return getHost()?.id === target.id;
}
exports.isHost = isHost;
