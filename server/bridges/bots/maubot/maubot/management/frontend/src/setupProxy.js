const proxy = require("http-proxy-middleware")

module.exports = function(app) {
    app.use(proxy("/chat/maubot/v1", { target: process.env.PROXY || "http://localhost:29316" }))
    app.use(proxy("/chat/maubot/v1/logs", { target: process.env.PROXY || "http://localhost:29316", ws: true }))
}
