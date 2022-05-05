// stub for browser-matrix browserify tests
global.XMLHttpRequest = jest.fn();

afterAll(() => {
    // clean up XMLHttpRequest mock
    global.XMLHttpRequest = undefined;
});
