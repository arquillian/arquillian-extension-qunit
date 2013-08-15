base_2 = [ "http://", window.location.host, "/ticket-monster/" ].join("");

// wrong URL by intention in order to verify that the extension works even if the QUnit Test suite is stuck
base_3 = [ "http://", window.location.host, "/ticket-monster-false/" ].join("");


module('Ajax calls');

test('Read events 1', function() {
    expect(7);
    stop();
    $.ajax({
        url : (base_2 + "rest/events")
    }).done(function(data) {
        console.debug(data);
        equal(data.length, 19, "Received 19 events");
        event = data[7];
        notEqual(event.id, undefined, "Event is properly formed");
        notEqual(event.name, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.category, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.mediaItem, undefined, "Event is properly formed");
        start();
    })
});

test('Read events 2', function() {
    expect(7);
    stop();
    $.ajax({
        url : (base_3 + "rest/events")
    }).done(function(data) {
        console.debug(data);
        equal(data.length, 19, "Received 19 events");
        event = data[7];
        notEqual(event.id, undefined, "Event is properly formed");
        notEqual(event.name, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.category, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.mediaItem, undefined, "Event is properly formed");
        start();
    })
});

test('Read events 3', function() {
    expect(7);
    stop();
    $.ajax({
        url : (base_3 + "rest/events")
    }).done(function(data) {
        console.debug(data);
        equal(data.length, 19, "Received 19 events");
        event = data[7];
        notEqual(event.id, undefined, "Event is properly formed");
        notEqual(event.name, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.category, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.mediaItem, undefined, "Event is properly formed");
        start();
    })
});

test('Read events 4', function() {
    expect(7);
    stop();
    $.ajax({
        url : (base_3 + "rest/events")
    }).done(function(data) {
        console.debug(data);
        equal(data.length, 19, "Received 19 events");
        event = data[7];
        notEqual(event.id, undefined, "Event is properly formed");
        notEqual(event.name, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.category, undefined, "Event is properly formed");
        notEqual(event.description, undefined, "Event is properly formed");
        notEqual(event.mediaItem, undefined, "Event is properly formed");
        start();
    })
});