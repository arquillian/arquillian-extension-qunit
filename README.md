# Arquillian QUnit
[Arquillian QUnit](https://github.com/arquillian/arquillian-extension-qunit) is an [Arquillian](http://arquillian.org/) extension which automates QUnit JavaScript unit testing. It integrates transparently with the JUnit testing framework and can be easily used in continuous integration environments.

Arquillian-Qunit identifies the number of QUnit tests that have to be executed before the actual QUnit tests execution. In the case of a stuck QUnit Test Suite, the stuck tests are marked as errored.

For instance, when executing these [QUnit tests](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/resources/assets/tests/rest-service/qunit-tests-stuck.html), the QUnit Test Suite gets stuck as shown in the below image. Arquillian-QUnit marks the stuck tests as errored.

![Stuck QUnit Test Suite](https://raw.github.com/tolis-e/readme-images/master/qunit-stuck-test.png)

In order to verify this kind of QUnit Test Suite validation, uncomment the corresponding [showcase test](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/java/org/jboss/arquillian/qunit/junit/ftest/QUnitRunnerTestCase.java#L79) and execute the functional test.

The execution results will be:

![Results](https://raw.github.com/tolis-e/readme-images/master/arquillian-qunit-stuck-tests-report.png)


## Execute QUnit tests through an IDE
You can execute the [QUnitRunnerTestCase](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/java/org/jboss/arquillian/qunit/junit/ftest/QUnitRunnerTestCase.java) through your favorite IDE.

The results for the showcase example will be:

![Results](https://raw.github.com/tolis-e/readme-images/master/ARQ-QUnit-IDE.png)

## Arquillian QUnit Functional Test / Showcase
This project contains the functional test / showcase for the Arquillian-QUnit project. Navigate to the arquillian-qunit-ftest project and execute:

    mvn test -P arq-jbossas-managed
 
The Arquillian QUnit Functional Test defines the three core aspects needed for the execution of an [Arquillian](http://arquillian.org/) test case:

- container — the runtime environment
- deployment — the process of dispatching an artifact to a container
- archive — a packaged assembly of code, configuration and resources

The container's configuration resides in the [Arquillian XML](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/resources/arquillian.xml) configuration file while the deployment and the archive are defined inside the [QUnitRunnerTestCase](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/java/org/jboss/arquillian/qunit/junit/ftest/QUnitRunnerTestCase.java) file.

The test case is dispatched to the container's environment through coordination with ShrinkWrap, which is used to declaratively define a custom Java EE archive that encapsulates the test class and its dependent resources. Arquillian packages the ShrinkWrap defined archive at runtime and deploys it to the target container. It then negotiates the execution of the test methods and captures the test results using remote communication with the server. Finally, Arquillian undeploys the test archive.

The [POM](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/pom.xml) configuration file contains two profiles:

* arq-jboss-managed — managed container 
* arq-jboss-remote — remote container

An Arquillian managed container is a remote container whose lifecycle is managed by Arquillian. The specific profile is also configured to download and unpack the JBoss Application Server 7.1.1.Final distribution zip from the Maven Central repository.

### Instructions to setup a new test class

* Add the Arquillian-QUnit dependency to the POM file:
    
        <dependency>
            <groupId>org.jboss.arquillian.extension</groupId>
            <artifactId>arquillian-qunit</artifactId>
            <version>${project.version}</version>
        </dependency>
* Add the `arquillian-junit-container` (whole lifecycle of the container and deployment will be managed by Arquillian) or `arquillian-junit-standalone` dependency.

        <dependency>
            <groupId>org.jboss.arquillian.junit</groupId>
            <artifactId>arquillian-junit-container</artifactId>
        </dependency>

        <dependency>
            <groupId>org.jboss.arquillian.junit</groupId>
            <artifactId>arquillian-junit-standalone</artifactId>
        </dependency>
* To create an Arquillian QUnit test class, annotate the test class with the `@RunWith(QUnitRunner.class)` and `@QUnitResources` annotations. The `@QUnitResources` annotation’s value should be the path of the folder which contains all the QUnit resources and dependencies which are required for your QUnit tests execution. For each QUnit Test Suite that you would like to execute, create a method and annotate it with the `@QUnitTest` annotation. The annotation’s value should be the QUnit Test Suite path, relative to the `@QUnitResources` path.
* Note that each QUnit Test Suite should be completed in a time period of 2 minutes.



## Known Limitations
* The run/execution time printed in the report is not accurate.
* Multiple deployments are not supported.

## Documentation

* [Arquillian Guides](http://arquillian.org/guides/)
* [Create Deployable Archives with ShrinkWrap](http://arquillian.org/guides/shrinkwrap_introduction/)

## Releasing new version

Run following commands:

    mvn clean release:prepare release:perform

Make sure that you push the tag, close all issues with given version in JIRA and mark version as released.
