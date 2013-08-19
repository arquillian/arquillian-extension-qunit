# Arquillian QUnit
[Arquillian QUnit](https://github.com/arquillian/arquillian-extension-qunit) is an [Arquillian](http://arquillian.org/) extension which automates QUnit JavaScript unit testing. It integrates transparently with the JUnit testing framework and therefore can be easily used in continuous integration environments.

Arquillian-Qunit knows the number of QUnit tests that have to be executed before the actual QUnit tests execution. In the case where a Qunit Test Suite gets stuck because of a stuck QUnit test, Arquillian-QUnit marks the stuck test and the rest of QUnit tests which are not executed/reached as failed.

For example when executing these [QUnit tests](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/resources/assets/tests/ticketmonster/test-stuck.js) you will realize that the QUnit Test Suite gets stuck as shown in the below image. Arquillian-QUnit knows that these tests did not finish and marks them as failed.

![Stuck QUnit Test Suite](https://raw.github.com/tolis-e/readme-images/master/qunit-stuck-test.png)

In order to verify this kind of QUnit Test Suite validation, uncomment the corresponding [showcase test](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/java/org/jboss/arquillian/qunit/junit/ftest/QUnitRunnerTestCase.java#L76) and execute the functional test.

The execution results will be:

![Results](https://raw.github.com/tolis-e/readme-images/master/arquillian-qunit-stuck-tests-report.png)

## Arquillian-QUnit Installation
To install it navigate to the arquillian-extension-qunit folder and execute:

    mvn install

## Arquillian QUnit Functional Test / Showcase
This project contains the functional test / showcase for the Arquillian-QUnit project. Navigate to the arquillian-qunit-ftest project and execute:

    mvn test
 
The Arquillian QUnit Functional Test defines the three core aspects needed for the execution of an [Arquillian](http://arquillian.org/) test case:

- container — the runtime environment
- deployment — the process of dispatching an artifact to a container
- archive — a packaged assembly of code, configuration and resources

The container's configuration resides in the [Arquillian XML](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/resources/arquillian.xml) configuration file while the deployment and the archive are defined inside the [QUnitRunnerTestCase](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/src/test/java/org/jboss/arquillian/qunit/junit/ftest/QUnitRunnerTestCase.java) file.

The test case is dispatched to the container's environment through coordination with ShrinkWrap, which is used to declaratively define a custom Java EE archive that encapsulates the test class and its dependent resources. Arquillian packages the ShrinkWrap defined archive at runtime and deploys it to the target container. It then negotiates the execution of the test methods and captures the test results using remote communication with the server. Finally, Arquillian undeploys the test archive.

The [POM](https://github.com/arquillian/arquillian-extension-qunit/blob/master/arquillian-qunit-ftest/pom.xml) configuration file contains two profiles:

* arq-jboss-managed — managed container 
* arq-jboss-remote — remote container

By default the arq-jboss-managed (managed container) profile is active. An Arquillian managed container is a remote container whose lifecycle is managed by Arquillian. The specific profile is also configured to download and unpack the JBoss Application Server 7.1.1.Final distribution zip from the Maven Central repository.

### Instructions to setup a new test case

* Install the Arquillian-QUnit to the local Maven repository
* Add the Arquillian-QUnit dependency to the POM file:
    
        <dependency>
            <groupId>org.jboss.arquillian.qunit</groupId>
            <artifactId>arquillian-qunit-impl</artifactId>
            <version>${version.org.jboss.arquillian.qunit.impl}</version>
        </dependency>

* Create a new Java Class which will be the test case and configure the below annotations in TYPE/Class level:
    * `@RunWith(QUnitRunner.class)` — Instructs JUnit to use the QUnitRunner as test controller.
    * `@QUnitResources("src/test/resources/assets")` — Points to the assets folder where the QUnit HTML Test Files, QUnit JS, JQuery JS reside.
* In some cases you might want to run your tests against a deployed archive. In such cases add a public static method with the `@Deployment` annotation inside the test case. This method should create the archive which will be deployed on the container.
* If you would like to execute the QUnit Test Suites without deploying any archive then do not include a method with the `@Deployment` annotation inside your test.
* Sometimes you might want the QUnit Test Suites and their relevant resources to be packaged, deployed and executed on a container. This can be done by defining a method with the `@Deployment` annotation and returning null `return null;`.
* Create as many methods inside the test case as the Qunit Test Suites you want to execute. Each method must have the `@QUnitTest()` annotation which points to a QUnit HTML Test file.
* In method level you can use the `@InSequence()` annotation to define the execution order.

### Sample Test Case

     package org.jboss.arquillian.qunit.junit.ftest;
     
     import java.io.File;
     
     import org.jboss.arquillian.container.test.api.Deployment;
     import org.jboss.arquillian.container.test.api.RunAsClient;
     import org.jboss.arquillian.junit.InSequence;
     import org.jboss.arquillian.qunit.junit.QUnitRunner;
     import org.jboss.arquillian.qunit.junit.annotations.QUnitResources;
     import org.jboss.arquillian.qunit.junit.annotations.QUnitTest;
     import org.jboss.shrinkwrap.api.Archive;
     import org.jboss.shrinkwrap.api.ShrinkWrap;
     import org.jboss.shrinkwrap.api.spec.WebArchive;
     import org.junit.runner.RunWith;
     
     /**
      * Junit uses the QunitRunner controller as a test controller.
      *
      * @author Lukas Fryc
      * @author Tolis Emmanouilidis
      *
      */
     @RunWith(QUnitRunner.class)
     @QUnitResources("src/test/resources/assets")
     public class QUnitRunnerTestCase {
     
         private static final String DEPLOYMENT = "src/test/resources/archives/ticket-monster.war";
     
         /**
          * Creates the Archive which will be finally deployed on the AS.
          *
          * @return Archive
          */
         @Deployment
         public static Archive<?> createDeployment() {
             return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT));
         }
     
         /**
          * Test the qunit-tests-ajax.html file.
          */
         @QUnitTest("tests/ticketmonster/qunit-tests-ajax.html")
         @InSequence(1)
         public void qunitAjaxTest() {
             // empty body - only the annotations are used
         }
     
         /**
          * Test the qunit-tests.html file.
          */
         @QUnitTest("tests/generic/qunitTest.html")
         @InSequence(2)
         public void qunitAssertionsTest() {
             // empty body - only the annotations are used
         }
     
     }
     
## Known Limitations
* The run/execution time printed in the report is not accurate.
* Multiple deployments are not supported.

## Documentation

* [Arquillian Guides](http://arquillian.org/guides/)
* [Create Deployable Archives with ShrinkWrap](http://arquillian.org/guides/shrinkwrap_introduction/)
