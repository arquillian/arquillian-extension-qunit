/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
(function(window) {
    
    function innerText(element) {
        if (!element) {
            return null;
        } else {
            return element.innerText || element.textContent;
        }
    }
    
    function getFailedAssertions(list) {
        var assertions = [];
        if (list) {
            for (var i=0; i<list.length; i++) {
                var text = innerText(list[i].querySelector(".test-source td pre"));
                if (text != null) {
                    assertions.push(text);
                }
            }
        }
        return assertions;
    }
    
    
    var elements = window.document.querySelectorAll("#qunit-tests > li[class~=pass], #qunit-tests > li[class~=fail]"),
        element,
        item;
    
    window.arquillianQunitSuiteResults = [];
    
    for ( var i = 0; i < elements.length; i++) {
        element = elements[i];
        item = [];
        item.push(innerText(element.querySelector("span.module-name")));
        item.push(innerText(element.querySelector("span.test-name")));
        item.push(innerText(element.querySelector("span.runtime")));
        item.push(element.getAttribute("class"));
        item.push(innerText(element.querySelector(".failed")));
        item.push(innerText(element.querySelector(".passed")));
        item.push(getFailedAssertions(element.querySelectorAll(".qunit-assert-list > li[class~=fail]")));
        window.arquillianQunitSuiteResults.push(item);
    }
    
})(this);