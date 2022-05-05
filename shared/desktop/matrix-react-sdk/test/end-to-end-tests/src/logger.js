module.exports = class Logger {
    constructor(username) {
        this.indent = 0;
        this.username = username;
        this.muted = false;
    }

    startGroup(description) {
        if (!this.muted) {
            const indent = " ".repeat(this.indent * 2);
            console.log(`${indent} * ${this.username} ${description}:`);
        }
        this.indent += 1;
        return this;
    }

    endGroup() {
        this.indent -= 1;
        return this;
    }

    step(description) {
        if (!this.muted) {
            const indent = " ".repeat(this.indent * 2);
            process.stdout.write(`${indent} * ${this.username} ${description} ... `);
        }
        return this;
    }

    done(status = "done") {
        if (!this.muted) {
            process.stdout.write(status + "\n");
        }
        return this;
    }

    mute() {
        this.muted = true;
        return this;
    }

    unmute() {
        this.muted = false;
        return this;
    }
};
