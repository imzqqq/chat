import { parseQsFromFragment, parseQs } from "../../src/vector/url_utils";

describe("url_utils.ts", function() {
    // @ts-ignore
    const location: Location = {
        hash: "",
        search: "",
    };

    it("parseQsFromFragment", function() {
        location.hash = "/home?foo=bar";
        expect(parseQsFromFragment(location)).toEqual({
            location: "home",
            params: {
                "foo": "bar",
            },
        });
    });

    describe("parseQs", function() {
        location.search = "?foo=bar";
        expect(parseQs(location)).toEqual({
            "foo": "bar",
        });
    });

    describe("parseQs with arrays", function() {
        location.search = "?via=s1&via=s2&via=s2&foo=bar";
        expect(parseQs(location)).toEqual({
            "via": ["s1", "s2", "s2"],
            "foo": "bar",
        });
    });
});
