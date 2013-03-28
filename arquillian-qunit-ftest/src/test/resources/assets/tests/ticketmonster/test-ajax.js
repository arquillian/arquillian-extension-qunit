//QUnit.config.reorder = false;
base = [ "http://", window.location.host, "/ticket-monster/" ].join("");

module('Ajax calls');

test('Read events', function() {
    expect(7);
    stop();
    $.ajax({
        url : (base + "rest/events")
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