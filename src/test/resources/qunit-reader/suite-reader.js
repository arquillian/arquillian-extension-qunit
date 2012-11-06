(function(window) {

	function extend(a, b) {
		for ( var prop in b) {
			if (b[prop] === undefined) {
				delete a[prop];

				// Avoid "Member not found" error in IE8 caused by setting
				// window.constructor
			} else if (prop !== "constructor" || a !== window) {
				a[prop] = b[prop];
			}
		}

		return a;
	}

	function print(text) {
		window.tests = window.tests || [];
		window.tests.push(text);
	}
	
	function ready(text) {
        window.tests = window.tests || [];
        window.tests.push(text);
    }

	var QUnit = {

		currentModule : null,

		module : function(name) {
			currentModule = name;
		},

		asyncTest : function(testName) {
			print(currentModule + ": " + testName);
		},

		test : function(testName, expected, callback, async) {
			print(currentModule + ": " + testName);
		},

		expect : function() {

		},

		ok : function() {

		},

		equal : function() {
		},

		notEqual : function(actual, expected, message) {
		},

		deepEqual : function(actual, expected, message) {
		},

		notDeepEqual : function(actual, expected, message) {
		},

		strictEqual : function(actual, expected, message) {
		},

		notStrictEqual : function(actual, expected, message) {
		},

		raises : function() {
		},

		start : function() {
		},

		stop : function() {
		},
		
		done : function() {
		}
	};

	extend(window, QUnit);
	window.QUnit = QUnit;
	
	document.addEventListener('DOMContentLoaded',function(){
	    
	})

})(this);