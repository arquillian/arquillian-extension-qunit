## Arquillian QUnit
The [Arquillian QUnit](https://github.com/tolis-e/arquillian-qunit) is an [Arquillian](http://arquillian.org/) extension which automates the [QUnit](http://qunitjs.com/) JavaScript testing on Web Applications. It smoothly integrates with the JUnit testing framework in order to produce a results report. In case of test failures it indicates the specific QUnit JavaScript Test File and the specific line/s in which the error/s occurred.

## Arquillian QUnit API
The [Arquillian QUnit API](https://github.com/tolis-e/arquillian-qunit/tree/master/arquillian-qunit-api) is the API which abstracts the [Arquillian QUnit](https://github.com/tolis-e/arquillian-qunit/tree/master/arquillian-qunit-impl) implementation.

## Arquillian QUnit Functional Test
This project contains the functional and acceptance tests for the Arquillian-QUnit project.

## Execution Order

* Navigate to the arquillian-qunit-api project and execute:

    `mvn install`

* Navigate to the arquillian-qunit-impl project and execute:

    `mvn install`

* Navigate to the arquillian-qunit-ftest project and execute:

    `mvn test`

For detailed information check the README file inside each project.
