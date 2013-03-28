//A deep recursive comparison assertion, working on primitive types, arrays, objects, regular expressions, dates and functions.
test("deepEqual test", function() {
    var obj = {
        bar : "foo",
        foo : "bar"
    };
    deepEqual(obj, {
        foo : "bar",
        bar : "foo"
    }, "Two objects can be the same in value");
});

// An inverted deep recursive comparison assertion, working on primitive types,
// arrays, objects, regular expressions, dates and functions.
test("notDeepEqual test", function() {
    var obj = {
        foo : "bar"
    };
    notDeepEqual(obj, {
        foo : "bla"
    }, "Different object, same key, different value, not equal");
});

// A non-strict comparison assertion, roughly equivalent to JUnit assertEquals
test("equal test", function() {
    equal(0, 0, "Zero; equal succeeds");
    equal("", 0, "Empty, Zero; equal succeeds");
    equal("", "", "Empty, Empty; equal succeeds");
    equal(0, "0", "Zero, Zero; equal succeeds");
    equal(3, 3, "3, 3; equal succeeds");
});

// A non-strict comparison assertion, checking for inequality.
test("a test", function() {
    notEqual(3, "2", "String '3' and number 2 don't have the same value");
});

// strict comparison by value and type
test("a test",
        function() {
            notStrictEqual(1, "1",
                    "String '1' and number 1 don't have the same value");
        });

// set module
module("module1")

// strict comparison by value and type
test("strictEqual test", function() {
    strictEqual(1, 1, "1 and 1 are the same value and type");
});

// Assertion to test if a callback throws an exception when run.
test("throws", function() {
    function CustomError(message) {
        this.message = message;
    }
    CustomError.prototype.toString = function() {
        return this.message;
    };
    throws(function() {
        throw "error"
    }, "throws with just a message, no expected");
    throws(function() {
        throw new CustomError();
    }, CustomError, "raised error is an instance of CustomError");
    throws(function() {
        throw new CustomError("some error description");
    }, /description/, "raised error message contains 'description'");
});