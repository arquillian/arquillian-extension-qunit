# Arquillian QUnit
The [Arquillian QUnit](https://github.com/tolis-e/arquillian-qunit) is an [Arquillian](http://arquillian.org/) extension which automates the QUnit JavaScript testing on Web Applications. [Arquillian](http://arquillian.org/) integrates transparently with the testing framework which is JUnit in this case and produces a results report. In case of test failures it indicates the specific QUnit JavaScript Test File and the specific line/s in which the error/s occurred.

## Installation
Before executing the functional test, install the Arquillian-QUnit-API artifact is installed to your Maven repository.
To install it navigate to the arquillian-qunit-api project and execute:

    mvn install
    
To install it navigate to the arquillian-qunit-impl project and execute:

    mvn install