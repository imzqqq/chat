module.exports.range = function(start, amount, step = 1) {
    const r = [];
    for (let i = 0; i < amount; ++i) {
        r.push(start + (i * step));
    }
    return r;
};

module.exports.delay = function(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
};

module.exports.measureStart = function(session, name) {
    return session.page.evaluate(_name => {
        window.mxPerformanceMonitor.start(_name);
    }, name);
};

module.exports.measureStop = function(session, name) {
    return session.page.evaluate(_name => {
        window.mxPerformanceMonitor.stop(_name);
    }, name);
};
