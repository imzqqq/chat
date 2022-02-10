// -*- coding: utf-8 -*-
// Copyright 2019, 2020 The Matrix.org Foundation C.I.C.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _peerInfo = _interopRequireDefault(require("peer-info"));

var _peerId = _interopRequireDefault(require("peer-id"));

var _browserBundle = _interopRequireDefault(require("./browser-bundle"));

var _cids = _interopRequireDefault(require("cids"));

var _multihashingAsync = _interopRequireDefault(require("multihashing-async"));

var _keys = _interopRequireDefault(require("libp2p-crypto/src/keys"));

var _pullStream = require("pull-stream");

var _concat = _interopRequireDefault(require("pull-stream/sinks/concat"));

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

var generateKeyPairFromSeed = (0, _es6Promisify.promisify)(_keys["default"].generateKeyPairFromSeed);
var createPeerInfo = (0, _es6Promisify.promisify)(_peerInfo["default"].create);
var createPeerId = (0, _es6Promisify.promisify)(_peerId["default"].create);
var createFromPrivKey = (0, _es6Promisify.promisify)(_peerId["default"].createFromPrivKey);

var P2pLocalNode = /*#__PURE__*/function () {
  /**
   * Construct a new P2P local node
   * @param { } service  service name
   * @param {*} seed Uint8Array: the 32 byte ed25519 private key seed (RFC 8032)
   * @param {*} addrs addresses to listen for traffic on.
   * @param {string} cbNamespace The namespace that will be called on _go_js_server, defaults to 'p2p'.
   */
  function P2pLocalNode(service, seed, addrs, cbNamespace) {
    _classCallCheck(this, P2pLocalNode);

    console.log("P2pLocalNode called with ".concat(service, " and ").concat(addrs, " with seed"));
    this.service = service;
    this.addrs = addrs;
    this.seed = seed;
    this.cbNamespace = cbNamespace || 'p2p';
  }

  _createClass(P2pLocalNode, [{
    key: "stop",
    value: function stop() {
      this.node.stop();
    }
  }, {
    key: "init",
    value: function () {
      var _init = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee2() {
        var _this = this;

        var key, peerId, peerInfo, peerIdStr, _iterator, _step, addr, node, discoveredPeersSet, connPeersSet, hash, cid, findProviders, provideContent, protocol;

        return regeneratorRuntime.wrap(function _callee2$(_context2) {
          while (1) {
            switch (_context2.prev = _context2.next) {
              case 0:
                _context2.next = 2;
                return generateKeyPairFromSeed("ed25519", this.seed);

              case 2:
                key = _context2.sent;
                _context2.next = 5;
                return createFromPrivKey(key.bytes);

              case 5:
                peerId = _context2.sent;
                peerInfo = new _peerInfo["default"](peerId);
                peerIdStr = peerInfo.id.toB58String();
                _iterator = _createForOfIteratorHelper(this.addrs);

                try {
                  for (_iterator.s(); !(_step = _iterator.n()).done;) {
                    addr = _step.value;
                    peerInfo.multiaddrs.add(addr);
                  }
                } catch (err) {
                  _iterator.e(err);
                } finally {
                  _iterator.f();
                }

                console.log(peerIdStr, ": Starting up");
                node = new _browserBundle["default"]({
                  peerInfo: peerInfo
                });
                this.node = node;
                this.idStr = peerIdStr;
                discoveredPeersSet = new Set();
                node.on('peer:discovery', function (pi) {
                  discoveredPeersSet.add(pi.id.toB58String());

                  if (discoveredPeersSet.size < 20) {
                    console.debug('Discovered a peer:', pi.id.toB58String());
                  } else if (discoveredPeersSet.size === 20) {
                    console.debug("Discovered many peers: silencing output.");
                  } // tell go


                  if (_this.onPeerDiscover) _this.onPeerDiscover(pi);
                });
                connPeersSet = new Set();
                node.on('peer:connect', function (pi) {
                  var idStr = pi.id.toB58String();
                  connPeersSet.add(idStr);

                  if (connPeersSet.size < 20) {
                    console.debug('Got connection to: ' + idStr);
                  } else if (connPeersSet.size === 20) {
                    console.debug("Connected to many peers: silencing output.");
                  } // tell go


                  if (_this.onPeerConnect) _this.onPeerConnect(pi);
                });
                node.on('peer:disconnect', function (pi) {
                  var idStr = pi.id.toB58String();
                  connPeersSet["delete"](idStr);
                  console.debug(idStr + " went away"); // tell go

                  if (_this.onPeerDisonnect) _this.onPeerDisconnect(pi);
                });
                _context2.next = 21;
                return (0, _multihashingAsync["default"])(Buffer.from(this.service), 'sha2-256');

              case 21:
                hash = _context2.sent;
                cid = new _cids["default"](1, 'dag-pb', hash);

                findProviders = function findProviders() {
                  node.contentRouting.findProviders(cid, {
                    maxTimeout: 5000
                  }, function (err, providers) {
                    if (err) {
                      setTimeout(findProviders, 30 * 1000);
                      console.error("Failed to find providers:", err);
                      return;
                    }

                    console.log('Connected peers: ', connPeersSet.size, " Discovered peers: ", discoveredPeersSet.size, ' Found providers:', providers.map(function (p) {
                      return p.id.toB58String();
                    }));
                    providers = providers.filter(function (p) {
                      return p.id.toB58String() != peerIdStr;
                    });

                    if (_this.onFoundProviders) {
                      _this.onFoundProviders(providers);
                    }

                    setTimeout(findProviders, 5000);
                  });
                };

                provideContent = function provideContent() {
                  console.log(peerIdStr, ": Attempting to provide ", cid.toBaseEncodedString()); // advertise our magic CID to announce our participation in this specific network

                  node.contentRouting.provide(cid, function (err) {
                    if (err) {
                      console.error("failed to provide CID:", cid, err);
                      throw err;
                    }

                    console.log('Node %s is providing %s', peerIdStr, cid.toBaseEncodedString());
                  });
                  setTimeout(provideContent, 1000 * 60 * 5); // every 5min
                };

                node.start(function (err) {
                  if (err) {
                    console.error(peerIdStr, ": p2p start node error:", err);
                    return;
                  }

                  provideContent();
                  console.log("".concat(peerIdStr, " is listening o/"));
                  node.peerInfo.multiaddrs.toArray().forEach(function (ma) {
                    console.log("Listening on: ", ma.toString());
                  });
                  findProviders(); // NOTE: to stop the node
                  // node.stop((err) => {})
                });
                protocol = '/libp2p-http/1.0.0';
                node.handle(protocol, /*#__PURE__*/function () {
                  var _ref = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee(protocol, conn) {
                    var getPeerInfo, pi, reqBuffer, reqResolve, reqPromise, reqString, result, respString;
                    return regeneratorRuntime.wrap(function _callee$(_context) {
                      while (1) {
                        switch (_context.prev = _context.next) {
                          case 0:
                            getPeerInfo = (0, _es6Promisify.promisify)(conn.getPeerInfo.bind(conn));
                            _context.next = 3;
                            return getPeerInfo();

                          case 3:
                            pi = _context.sent;
                            console.log(peerIdStr, ": incoming conn from ", pi.id.toB58String());
                            reqBuffer = '';
                            reqPromise = new Promise(function (resolve, reject) {
                              reqResolve = resolve;
                            });
                            (0, _pullStream.pull)(conn, (0, _concat["default"])(function (err, data) {
                              if (err) throw err;
                              reqBuffer = data;
                              reqResolve(reqBuffer);
                            }));
                            _context.next = 10;
                            return reqPromise;

                          case 10:
                            reqString = _context.sent;
                            _context.next = 13;
                            return global._go_js_server[_this.cbNamespace](reqString);

                          case 13:
                            result = _context.sent;
                            respString = '';

                            if (result.error) {
                              console.error("p2pLocalNode: Error for request: ".concat(result.error));
                              console.error(reqString); // TODO: Send some error response?
                            } else {
                              respString = result.result;
                              console.log(respString);
                            }

                            (0, _pullStream.pull)(_pullStream.pull.values([respString]), conn);

                          case 17:
                          case "end":
                            return _context.stop();
                        }
                      }
                    }, _callee);
                  }));

                  return function (_x, _x2) {
                    return _ref.apply(this, arguments);
                  };
                }());
                console.log(peerIdStr, ": awaiting p2p connections for protocol: ", protocol);

              case 29:
              case "end":
                return _context2.stop();
            }
          }
        }, _callee2, this);
      }));

      function init() {
        return _init.apply(this, arguments);
      }

      return init;
    }() // implemented in Go:
    // onPeerDiscover(peerInfo) {}
    // onPeerConnect(peerInfo) {}
    // onPeerDisconnect(peerInfo) {}
    // onFoundProviders([]peerInfo) {}

  }]);

  return P2pLocalNode;
}();

exports["default"] = P2pLocalNode;