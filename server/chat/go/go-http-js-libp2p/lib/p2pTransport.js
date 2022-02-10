"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _pullStream = require("pull-stream");

var _concat = _interopRequireDefault(require("pull-stream/sinks/concat"));

var _peerInfo = _interopRequireDefault(require("peer-info"));

var _peerId = _interopRequireDefault(require("peer-id"));

var _es6Promisify = require("es6-promisify");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function _createForOfIteratorHelper(o, allowArrayLike) { var it = typeof Symbol !== "undefined" && o[Symbol.iterator] || o["@@iterator"]; if (!it) { if (Array.isArray(o) || (it = _unsupportedIterableToArray(o)) || allowArrayLike && o && typeof o.length === "number") { if (it) o = it; var i = 0; var F = function F() {}; return { s: F, n: function n() { if (i >= o.length) return { done: true }; return { done: false, value: o[i++] }; }, e: function e(_e) { throw _e; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); } var normalCompletion = true, didErr = false, err; return { s: function s() { it = it.call(o); }, n: function n() { var step = it.next(); normalCompletion = step.done; return step; }, e: function e(_e2) { didErr = true; err = _e2; }, f: function f() { try { if (!normalCompletion && it["return"] != null) it["return"](); } finally { if (didErr) throw err; } } }; }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () { var self = this, args = arguments; return new Promise(function (resolve, reject) { var gen = fn.apply(self, args); function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

var P2pTransport = /*#__PURE__*/function () {
  function P2pTransport(p2pLocalNode) {
    _classCallCheck(this, P2pTransport);

    this.p2pLocalNode = p2pLocalNode;
  }

  _createClass(P2pTransport, [{
    key: "roundTrip",
    value: function () {
      var _roundTrip = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee(req) {
        var host, destPeerId, destPeerInfo, node, dial, conn, reqHeaders, _iterator, _step, header, reqString, respResolve, respPromise, respString, m, respHeaders, headerLines, _iterator2, _step2, headerLine, match, resp;

        return regeneratorRuntime.wrap(function _callee$(_context) {
          while (1) {
            switch (_context.prev = _context.next) {
              case 0:
                _context.prev = 0;
                // figure out what address we're connecting to
                // we can't use the url npm module, as it lowercases the host
                // we can't use the browser's Url module, as it doesn't parse hosts for unknown URI schemes
                console.log("p2pTransport: roundTrip: ", req.method, " ", req.url, " ", req.body);
                host = req.url.match(/^matrix:\/\/(.*?)\//)[1];
                destPeerId = _peerId["default"].createFromB58String(host);
                destPeerInfo = new _peerInfo["default"](destPeerId);
                this.p2pLocalNode.addrs.forEach(function (a) {
                  destPeerInfo.multiaddrs.add(a);
                }); // dial out over libp2p

                node = this.p2pLocalNode.node;
                dial = (0, _es6Promisify.promisify)(node.dialProtocol);
                console.log("p2pTransport: Dialling ", JSON.stringify(destPeerInfo));
                _context.next = 11;
                return dial(destPeerInfo, '/libp2p-http/1.0.0');

              case 11:
                conn = _context.sent;
                // the world's dumbest HTTP client.
                // it would be much better to hook up go's HTTP client (and then we'd get HTTP/2 etc)
                // but we can't because https://github.com/golang/go/issues/27495
                reqHeaders = '';

                if (req.headers) {
                  _iterator = _createForOfIteratorHelper(req.headers);

                  try {
                    for (_iterator.s(); !(_step = _iterator.n()).done;) {
                      header = _step.value;
                      // FIXME: is this a safe header encoding?
                      reqHeaders += "".concat(header[0], ": ").concat(header[1], "\n");
                    }
                  } catch (err) {
                    _iterator.e(err);
                  } finally {
                    _iterator.f();
                  }
                }

                if (req.method === "POST" || req.method === "PUT") {
                  reqHeaders += "Content-Length: ".concat(new Blob([req.body]).size); // include utf-8 chars properly
                }

                if (reqHeaders.length > 0) {
                  reqHeaders = "\r\n".concat(reqHeaders);
                }

                reqString = "".concat(req.method, " ").concat(req.url, " HTTP/1.0").concat(reqHeaders, "\r\n\r\n").concat(req.body);
                (0, _pullStream.pull)(_pullStream.pull.values([reqString]), conn);
                respPromise = new Promise(function (resolve, reject) {
                  respResolve = resolve;
                });
                (0, _pullStream.pull)(conn, (0, _concat["default"])(function (err, data) {
                  if (err) throw err;
                  respString = data;
                  respResolve();
                }));
                _context.next = 22;
                return respPromise;

              case 22:
                m = respString.match(/^(HTTP\/1.1) ((.*?) (.*?))(\r\n([^]*?)?(\r\n\r\n([^]*?)))?$/);

                if (!m) {
                  console.warn("p2pTransport: couldn't parse resp", respString);
                }

                respHeaders = [];
                headerLines = m[6].split('\r\n');
                _iterator2 = _createForOfIteratorHelper(headerLines);

                try {
                  for (_iterator2.s(); !(_step2 = _iterator2.n()).done;) {
                    headerLine = _step2.value;
                    // FIXME: is this safe header parsing? Do we need to worry about line-wrapping?
                    match = headerLine.match(/^(.+?): *(.*?)$/);

                    if (match) {
                      respHeaders.push([match[1], match[2]]);
                    } else {
                      console.log("p2pTransport: couldn't parse headerLine ", headerLine);
                    }
                  }
                } catch (err) {
                  _iterator2.e(err);
                } finally {
                  _iterator2.f();
                }

                console.log("p2pTransport: Response ", m[2]);
                console.log("p2pTransport: Response body: ", m[8]);
                resp = {
                  "proto": m[1],
                  "status": m[2],
                  "statusCode": parseInt(m[3]),
                  "headers": respHeaders,
                  "body": m[8]
                };
                return _context.abrupt("return", resp);

              case 34:
                _context.prev = 34;
                _context.t0 = _context["catch"](0);
                console.error("p2pTransport: round trip error: ", _context.t0);
                return _context.abrupt("return", {
                  "error": "p2pTransport.js error: " + _context.t0
                });

              case 38:
              case "end":
                return _context.stop();
            }
          }
        }, _callee, this, [[0, 34]]);
      }));

      function roundTrip(_x) {
        return _roundTrip.apply(this, arguments);
      }

      return roundTrip;
    }()
  }]);

  return P2pTransport;
}();

exports["default"] = P2pTransport;