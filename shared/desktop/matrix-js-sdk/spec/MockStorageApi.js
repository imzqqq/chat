/**
 * A mock implementation of the webstorage api
 * @constructor
 */
export function MockStorageApi() {
    this.data = {};
    this.keys = [];
    this.length = 0;
}

MockStorageApi.prototype = {
    setItem: function(k, v) {
        this.data[k] = v;
        this._recalc();
    },
    getItem: function(k) {
        return this.data[k] || null;
    },
    removeItem: function(k) {
        delete this.data[k];
        this._recalc();
    },
    key: function(index) {
        return this.keys[index];
    },
    _recalc: function() {
        const keys = [];
        for (const k in this.data) {
            if (!this.data.hasOwnProperty(k)) {
                continue;
            }
            keys.push(k);
        }
        this.keys = keys;
        this.length = keys.length;
    },
};

