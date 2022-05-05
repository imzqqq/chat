"use strict";

const fs = require("fs").promises;
const postcss = require('postcss');
const {parse} = require("postcss-scss");

const postcssPlugins = ["postcss-strip-inline-comments", "postcss-nested", "postcss-simple-vars", "postcss-color-function"].map((plugin) => require(plugin)());

let inputFile = `${__dirname}/src/style.css`;
let outputFile = `${__dirname}/build/bundle.css`;

function bundle() {
	fs.readFile(inputFile, "utf-8").then((input) => {
		return parse(input);
	}).then((ast) => {
		return postcss(postcssPlugins).process(ast, {
			from: "style.css",
			to: "bundle.css"
		});
	}).then((bundle) => {
		return fs.writeFile(outputFile, bundle.css);
	}).then(() => {
		console.log("Finished writing CSS bundle");
	});
}

if (process.env.NODE_ENV != "development") {
	bundle();
} else {
	const chokidar = require("chokidar");
	const debounce = require("debounce");
	console.log("Watching for changes");
	chokidar.watch(`${__dirname}/src`).on("all", debounce(() => {
		bundle();
	}, 100));
}