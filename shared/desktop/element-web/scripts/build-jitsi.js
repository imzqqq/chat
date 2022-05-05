// This is a JS script so that the directory is created in-process on Windows.
// If the script isn't run in-process, there's a risk of it racing or never running
// due to file associations in Windows.
// Sorry.

const fs = require("fs");
const path = require("path");
const mkdirp = require("mkdirp");
const fetch = require("node-fetch");
const ProxyAgent = require("simple-proxy-agent");

console.log("Making webapp directory");
mkdirp.sync("webapp");

// curl -s https://jitsi.riot.im/libs/external_api.min.js > ./webapp/jitsi_external_api.min.js
console.log("Downloading Meet script...");
const fname = path.join("webapp", "jitsi_external_api.min.js");

/// MARK - FIXME: Use self-build jitsi_external_api.min.js
fs.access(fname, fs.constants.F_OK, (err) => {
	if (err) {
		const options = {};
		if (process.env.HTTPS_PROXY) {
			options.agent = new ProxyAgent(process.env.HTTPS_PROXY, {tunnel: true});
		}

		/// MARK - imzqqq, TODO: use local deps instead
        fetch("https://cloud.imzqqq.top/api/v3/file/download/4tpIeQDC9SbwWxdD?sign=vgoSAEeZNU1-1Muz8rGzGhGHgYk-EC-QePJ2NTxLtbA%3D%3A1638129787", options).then(res => {
            // https://jitsi.riot.im/libs/external_api.min.js
			// fetch("https://127.0.0.1:8083/libs/external_api.min.js", options).then(res => {
			///
			const stream = fs.createWriteStream(fname);
			return new Promise((resolve, reject) => {
				res.body.pipe(stream);
				res.body.on('error', err => reject(err));
				res.body.on('finish', () => resolve());
			});
		}).then(() => console.log('Done with Meet download!'));
	} else {
		console.log("Meet script already exists!");
	}
});
///
