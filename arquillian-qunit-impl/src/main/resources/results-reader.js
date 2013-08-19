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
    
    function getElementsByAttributeValue(el, attrName, attrVal) {
        var results = [];
        var elements = el.getElementsByTagName("*");
        var element;
        for (var i = 0; i < elements.length; i++) {
            element = elements[i];
            var attribute = element.getAttribute(attrName);
            if (attribute && attribute === attrVal) {
                results.push(element);
            }
        }
        return results;
    }
    
    function getElementsByTagNameAndAttributeValue(el, tagName, attrName, attrVal) {
        var results = [];
        var elements = el.getElementsByTagName(tagName);
        var element;
        for (var i = 0; i < elements.length; i++) {
            element = elements[i];
            var attribute = element.getAttribute(attrName);
            if (attribute && attribute === attrVal) {
                results.push(element);
            }
        }
        return results;
    }
    
    function getFailedAssertions(list) {
        var assertions = [];
        if (list) {
            var element,
                className;
            for (var i=0; i<list.length; i++) {
                element = list[i],
                className = element.getAttribute("class");
                if (className && className === "fail") {
                    var children = element.childNodes;
                    if (children.length > 0) {
                        for (var k=0; k<children.length; k++) {
                            var child = children[k];
                            if (child && child.tagName.toUpperCase() === "TABLE") {
                                var testSourceTableRow = getElementsByAttributeValue(child, "class", "test-source");
                                if (testSourceTableRow && testSourceTableRow.length > 0) {
                                    var testSourceTableData = testSourceTableRow[0].getElementsByTagName("td");
                                    if (testSourceTableData && testSourceTableData.length > 0) {
                                        var text = innerText(testSourceTableData[0]);
                                        if (text != null) {
                                            assertions.push(text);
                                            break;
                                        }
                                    }    
                                }
                            }    
                        }
                    }
                }    
            }
        }
        return assertions;
    }
    
    var qunitTestElement = window.document.getElementById("qunit-tests"),
        elements = qunitTestElement.childNodes,
        element,
        className,
        item,
        moduleNameArr,
        testNameArr,
        runtimeArr,
        failedArr,
        passedArr,
        qunitAssertElement,
        qunitAssertList;
    
    window.arquillianQunitSuiteResults = [];
    
    for ( var i = 0; i < elements.length; i++) {
        element = elements[i];
        className = element.getAttribute("class");
        if (className && (className === "pass" || className === "fail")) {
            item = [];
            
            moduleNameArr = getElementsByTagNameAndAttributeValue(element, "span", "class", "module-name");
            testNameArr = getElementsByTagNameAndAttributeValue(element, "span", "class", "test-name");
            runtimeArr = getElementsByTagNameAndAttributeValue(element, "span", "class", "runtime");
            failedArr = getElementsByAttributeValue(element, "class", "failed");
            passedArr = getElementsByAttributeValue(element, "class", "passed");
            qunitAssertElement = getElementsByTagNameAndAttributeValue(element, "ol", "class", "qunit-assert-list");
            qunitAssertList = qunitAssertElement && qunitAssertElement.length > 0 ? qunitAssertElement[0].childNodes : null;
            
            item.push(innerText(moduleNameArr && moduleNameArr.length > 0 ? moduleNameArr[0] : null));
            item.push(innerText(testNameArr && testNameArr.length > 0 ? testNameArr[0] : null));
            item.push(innerText(runtimeArr && runtimeArr.length > 0 ? runtimeArr[0] : null));
            item.push(innerText(failedArr && failedArr.length > 0 ? failedArr[0] : null));
            item.push(innerText(passedArr && passedArr.length > 0 ? passedArr[0] : null));
            item.push(getFailedAssertions(qunitAssertList));
            window.arquillianQunitSuiteResults.push(item);
        }
    }
    
})(this);