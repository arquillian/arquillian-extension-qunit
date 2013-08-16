
//http://amalkov.blogspot.cz/2011/02/testing-your-web-application-with-qunit.html

var frame = {

    go : function(url) {
        var dfd = $.Deferred();

        $('#frame').attr('src', url);
        $('#frame').load(function() {
            dfd.resolve(window.frames[0].window.jQuery);
        });

        return dfd.promise();
    },

    load : function() {
        var dfd = $.Deferred();

        $('#frame').load(function() {
            dfd.resolve(window.frames[0].window.jQuery);
        });

        return dfd.promise();
    }
};
