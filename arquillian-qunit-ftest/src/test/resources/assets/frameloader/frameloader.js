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