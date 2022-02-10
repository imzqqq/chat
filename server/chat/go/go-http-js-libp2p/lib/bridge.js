"use strict";

var _p2pLocalNode = _interopRequireDefault(require("./p2pLocalNode.js"));

var _p2pTransport = _interopRequireDefault(require("./p2pTransport.js"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () { var self = this, args = arguments; return new Promise(function (resolve, reject) { var gen = fn.apply(self, args); function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

global._go_libp2p_nodes = [];
global._go_http_bridge = {
  newP2pLocalNode: function () {
    var _newP2pLocalNode = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee(service, seed, addrs, namespace) {
      var p2pLocalNode;
      return regeneratorRuntime.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              _context.prev = 0;
              p2pLocalNode = new _p2pLocalNode["default"](service, seed, addrs, namespace);
              _context.next = 4;
              return p2pLocalNode.init();

            case 4:
              global._go_libp2p_nodes.push(p2pLocalNode);

              return _context.abrupt("return", p2pLocalNode);

            case 8:
              _context.prev = 8;
              _context.t0 = _context["catch"](0);
              console.error("Failed to create newP2pLocalNode: ", _context.t0);

            case 11:
            case "end":
              return _context.stop();
          }
        }
      }, _callee, null, [[0, 8]]);
    }));

    function newP2pLocalNode(_x, _x2, _x3, _x4) {
      return _newP2pLocalNode.apply(this, arguments);
    }

    return newP2pLocalNode;
  }(),
  newP2pTransport: function newP2pTransport(p2pLocalNode) {
    return new _p2pTransport["default"](p2pLocalNode);
  }
};