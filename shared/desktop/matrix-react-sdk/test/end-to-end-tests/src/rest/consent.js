const request = require('request-promise-native');
const cheerio = require('cheerio');
const url = require("url");

module.exports.approveConsent = async function(consentUrl) {
    const body = await request.get(consentUrl);
    const doc = cheerio.load(body);
    const v = doc("input[name=v]").val();
    const u = doc("input[name=u]").val();
    const h = doc("input[name=h]").val();
    const formAction = doc("form").attr("action");
    const absAction = url.resolve(consentUrl, formAction);
    await request.post(absAction).form({ v, u, h });
};
