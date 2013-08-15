var url = [ "http://", window.location.host, "/ticket-monster" ].join("");

var jQ = function() {
};

module('DOM modifications')

test('Page loaded', function() {
    expect(1);
    stop();
    $.when(frame.go(url + "/index.html#events")).then(function(_$) {
        jQ = window.frames[0].$;
        console.debug(jQ("a"));
        notEqual(jQ, function() {
        }, "Obtained a jQuery reference");
        setTimeout(function() {
            start();
        }, 5000);
    });
});

test('Adding DOM elements', function() {
    expect(2);
    console.debug(jQ("#menu li"));
    before = jQ("#menu li").length;
    jQ("#menu ul").append("<li><a>Added</a></li>");
    equal(jQ("#menu li").length, before + 1, "List size changed");
    equal(jQ("#menu li a:contains('Added')").length, 1,
            "Added item is present in list");
});

test('DOM element removal', function() {
    expect(2);
    console.debug(jQ("#menu li"));
    before = jQ("#menu li").length;
    jQ("li:contains('Bookings')").detach();
    equal(jQ("#menu li").length, before - 1, "List size changed");
    equal(jQ("#menu li:contains('Bookings')").length, 0,
            "Removed item not in list");
});

test('Adding an attribute', function() {
    expect(4);
    element = jQ("a:contains('Venues')")
    console.debug(element)
    equal(element.length, 1, "Element exists")
    equal(element.prop("class"), "", "Class is not set yet");
    element.prop("class", "highlighted");
    jQ(".highlighted").css("background-color", "red");
    equal(element.prop("class"), "highlighted", "Class was changed");
    equal(element.css("background-color"), "rgb(255, 0, 0)",
            "CSS property was changed");
});

test('Attribute removal', function() {
    expect(3);
    element = jQ("a:contains('Venues')")
    equal(element.length, 1, "Element exists")
    ok(element.prop("href").indexOf("#venues") != -1, "Link reference is set");
    element.removeAttr("href")
    equal(element.prop("href"), "", "Link reference is set");
});
