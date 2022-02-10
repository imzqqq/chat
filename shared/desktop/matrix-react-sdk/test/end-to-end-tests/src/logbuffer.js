module.exports = class LogBuffer {
    constructor(page, eventName, eventMapper, reduceAsync=false, initialValue = "") {
        this.buffer = initialValue;
        page.on(eventName, (arg) => {
            const result = eventMapper(arg);
            if (reduceAsync) {
                result.then((r) => this.buffer += r);
            } else {
                this.buffer += result;
            }
        });
    }
};
