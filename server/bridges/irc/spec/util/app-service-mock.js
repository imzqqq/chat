"use strict";
const EventEmitter = require("events");
const util = require("util");
var instance = null;

function MockAppService() {
    this.expressApp = {
        post: (path, handler) => {
            if (path === '/chat/provision/link') {
                this.link = handler;
            }
            else if (path === '/chat/provision/unlink') {
                this.unlink = handler;
            }
        },
        get: (path, handler) => {
            if (path === '/chat/provision/listlinks/:roomId') {
                this.listLinks = handler;
            }
        },
        use: (req, res, next) => {
            //stub
        }
    };

    EventEmitter.call(this);
}
util.inherits(MockAppService, EventEmitter);

// Simulate a request to the link provisioning API
//  reqBody {object} - the API request body
//  statusCallback {function} - Called when the server returns a HTTP response code.
//  jsonCallback {function} - Called when the server returns a JSON object.
//  link {boolean} - true if this is a link request (false if unlink).
MockAppService.prototype._linkAction = function(reqBody, statusCallback, jsonCallback, link) {
    if (link ? !this.link : !this.unlink) {
        throw new Error("IRC AS hasn't hooked into link/unlink yet.");
    }

    const req = {
        body : reqBody,
        getId : () => 'test@' + Date.now()
    };

    const res = {
        status : function (number) { statusCallback(number); return res;},
        json : function (obj) { jsonCallback(obj); return res;},
    }

    if (link) {
        return this.link(req, res);
    }
    return this.unlink(req, res);
};

MockAppService.prototype._link = function(reqBody, statusCallback, jsonCallback) {
    return this._linkAction(reqBody, statusCallback, jsonCallback, true);
}

MockAppService.prototype._unlink = function(reqBody, statusCallback, jsonCallback) {
    return this._linkAction(reqBody, statusCallback, jsonCallback, false);
}

// Simulate a request to get provisioned mappings
//  reqParameters {object} - the API request parameters
//  statusCallback {function} - Called when the server returns a HTTP response code.
//  jsonCallback {function} - Called when the server returns a JSON object.
MockAppService.prototype._listLinks = function(reqParameters, statusCallback, jsonCallback) {
    const req = {
        params : reqParameters,
        getId : () => 'test@' + Date.now()
    };

    const res = {
        status : function (number) { statusCallback(number); return res;},
        json : function (obj) { jsonCallback(obj); return res;},
    }
    return this.listLinks(req, res);
}

MockAppService.prototype.listen = function(port) {
    // NOP
};

MockAppService.prototype._trigger = function(eventType, content) {
    if (content.user_id) {
        content.sender = content.user_id;
    }
    var listeners = instance.listeners(eventType);
    var promises = listeners.map(function(l) {
        return l(content);
    });

    if (eventType.indexOf("type:") === 0) {
        promises = promises.concat(this._trigger("event", content));
    }

    if (promises.length === 1) {
        return promises[0];
    }
    return Promise.all(promises);
};

MockAppService.prototype._queryAlias = function(alias) {
    if (!this.onAliasQuery) {
        throw new Error("IRC AS hasn't hooked into onAliasQuery yet.");
    }
    return this.onAliasQuery(alias).catch(function(err) {
        console.error("onAliasQuery threw => %s", err);
    });
};

MockAppService.prototype._queryUser = function(user) {
    if (!this.onUserQuery) {
        throw new Error("IRC AS hasn't hooked into onUserQuery yet.");
    }
    return this.onUserQuery(user).catch(function(err) {
        console.error("onUserQuery threw => %s", err);
    });
};

MockAppService.prototype.close = async function() { /* No-op */ };

function MockAppServiceProxy() {
    if (!instance) {
        instance = new MockAppService();
    }
    return instance;
}

MockAppServiceProxy.instance = function() {
    if (!instance) {
        instance = new MockAppService();
    }
    return instance;
};

MockAppServiceProxy.resetInstance = function() {
    if (instance) {
        instance.removeAllListeners();
    }
    instance = null;
};

module.exports = MockAppServiceProxy;
