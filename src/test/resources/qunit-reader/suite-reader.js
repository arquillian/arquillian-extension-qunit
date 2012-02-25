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
		var list = document.getElementsByTagName("ul")[0];
		var newListItem = document.createElement("li");
		newListItem.innerText = text;
		list.appendChild(newListItem);
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
		}
	};

	// Load paramaters
	(function() {
		var location = window.location || {
			search : "",
			protocol : "file:"
		}, params = location.search.slice(1).split("&"), length = params.length, urlParams = {}, current;

		if (params[0]) {
			for ( var i = 0; i < length; i++) {
				current = params[i].split("=");
				current[0] = decodeURIComponent(current[0]);
				// allow just a key to turn on a flag, e.g., test.html?noglobals
				current[1] = current[1] ? decodeURIComponent(current[1]) : true;
				urlParams[current[0]] = current[1];
			}
		}

		QUnit.urlParams = urlParams;

		// Figure out if we're running the tests from a server or not
		QUnit.isLocal = !!(location.protocol === 'file:');
	})();
	
	(function(){
		function loadScript(scriptLocation) {
			var list = document.getElementsByTagName("ul")[0];
			var newScript = document.createElement('script');
			newScript.type = 'text/javascript';
			newScript.src = scriptLocation;
			list.appendChild(newScript);
		}
		
		QUnit.readSuite = function() {
			var scripts = QUnit.urlParams['suite'];
			var root = QUnit.urlParams['root'] ? QUnit.urlParams['root'] : "";
			
			if (scripts) {
				var splitted = scripts.split('|');
				for (key in splitted) {
					loadScript(root + splitted[key]);
				}
			}
		}
	})();

	extend(window, QUnit);
	window.QUnit = QUnit;

})(this);