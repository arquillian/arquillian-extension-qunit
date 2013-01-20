var testsFound = arguments[0];

console.log('testsFound: ' + testsFound)

var elements, element, item;

if (testsFound == 0) {
    elements = document.querySelectorAll("#qunit-tests > li[class~=pass], #qunit-tests > li[class~=fail]");
} else {
    elements = document.querySelectorAll("#qunit-tests > li:nth-child(" + testsFound + ") ~ li[class~=pass], #qunit-tests > li:nth-child(" + testsFound + ") ~ li[class~=fail]");
}

var result = [];

for (var i = 0; i < elements.length; i++) {
    element = elements[i];
    item = [];
    item.push((element.querySelector("span.test-name") || {}).innerText);
    item.push((element.querySelector("span.module-name") || {}).innerText);
    item.push(element.getAttribute("class"));
    result.push(item);
}

console.log(result);

return result;