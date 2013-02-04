module("Warp");

test("warp test", function() {
    ok(true);

    Warp.initiative(function() {
        ajax.request();
    }).inspect("foo");
});