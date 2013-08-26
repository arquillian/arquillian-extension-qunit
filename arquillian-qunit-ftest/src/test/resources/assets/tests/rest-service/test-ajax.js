/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
base = [ "http://", window.location.host, "/rest-service/" ].join("");

module('Ajax calls');

test('Read cars', function() {
    expect(4);
    stop();
    $.ajax({
        url : (base + "rest/carservice/cars")
    }).done(function(data) {
    	ok(data.length === 1, "Cars received");
    	ok(data[0].color === "Black", "Color is black");
    	ok(data[0].numberFrame === "QWE123", "Number Frame is QWE123");
    	ok(data[0].model === "Model_1", "Model is Model_1");
        start();
    })
});