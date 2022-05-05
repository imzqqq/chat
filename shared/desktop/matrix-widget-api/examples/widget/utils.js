/* eslint-disable no-unused-vars */

function parseFragment() {
    const fragmentString = (window.location.hash || "?");
    return new URLSearchParams(fragmentString.substring(Math.max(fragmentString.indexOf('?'), 0)));
}

function assertParam(fragment, name) {
    const val = fragment.get(name);
    if (!val) throw new Error(`${name} is not present in URL - cannot load widget`);
    return val;
}

function handleError(e) {
    console.error(e);
    document.getElementById("container").innerText = "There was an error with the widget. See JS console for details.";
}
