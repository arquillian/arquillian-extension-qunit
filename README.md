# Arquillian QUnit
The [Arquillian QUnit](https://github.com/arquillian/arquillian-extension-qunit) is an [Arquillian](http://arquillian.org/) extension which automates the QUnit JavaScript testing on Web Applications. [Arquillian](http://arquillian.org/) integrates with the testing framework which is JUnit in this case.

## Installation
To install it navigate to the arquillian-qunit-parent project and execute:

    mvn install

## Arquillian QUnit API
[Arquillian QUnit API](https://github.com/arquillian/arquillian-extension-qunit/tree/master/arquillian-qunit-api)

## Arquillian QUnit Impl
The [Arquillian QUnit Impl](https://github.com/arquillian/arquillian-extension-qunit/tree/master/arquillian-qunit-impl) is an implementation of the [Arquillian QUnit API](https://github.com/arquillian/arquillian-extension-qunit/tree/master/arquillian-qunit-api).

## Arquillian QUnit Functional Test
This project contains the functional tests for the Arquillian-QUnit project.

### Functional Test 
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

### Functional Test Execution
The execution of the functional test is done through maven:

    mvn test

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
* In some cases we want our QUnit Tests to test a Web Application. In such cases you can setup your test case as following:
    * Add a method with the `@Deployment()` annotation inside the test case. This method should create the Archive which will be deployed on the container. For more information you may check the [Arquillian Create Deployable Archives with ShrinkWrap](http://arquillian.org/guides/shrinkwrap_introduction/) guide.
    * Insert the frameloader JavaScript file to the `<head>` section of the QUnit HTML test file by adding:

            <script type="text/javascript" src="../../frameloader/frameloader.js"></script>
    * Insert an `iframe` tag inside the `body` tag of your QUnit HTML Test file. The iframe will be used to load your actual test page inside the QUnit Test page.
    
            <iframe height="600" width="1000" id="frame"></iframe>
    * There are several ways to access the iframe's content. One of them is by using jQuery: `$('#iframeId').contents().find('#menu')`.
    * In order to avoid hardcoding the host/port on your JavaScript QUnit test Files you can retrieve them from the Window Object `window.location.host`. For more info check the Sample QUnit JS File below.  
* Create as many methods inside the test case as the Qunit Test files you want to execute. Each method must have the `@QUnitTest()` annotation which points to a QUnit HTML Test file.
* In method level you can use the `@InSequence()` annotation to define the execution order.

### Sample QUnit HTML Test File

    <!DOCTYPE html>
    <!--~
        ~ JBoss, Home of Professional Open Source
        ~ Copyright Red Hat, Inc., and individual contributors
        ~ by the @authors tag. See the copyright.txt in the distribution for a
        ~ full listing of individual contributors.
        ~
        ~ Licensed under the Apache License, Version 2.0 (the "License");
        ~ you may not use this file except in compliance with the License.
        ~ You may obtain a copy of the License at
        ~ http://www.apache.org/licenses/LICENSE-2.0
        ~ Unless required by applicable law or agreed to in writing, software
        ~ distributed under the License is distributed on an "AS IS" BASIS,
        ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        ~ See the License for the specific language governing permissions and
        ~ limitations under the License.
    -->
    <html>
        <head>
            <meta charset="UTF-8" />
            <title>HTML5 Test Suite</title>
            <link rel="stylesheet" href="../../qunit/qunit.css" type="text/css" media="screen">
            <script src="../../jquery/jquery-1.8.2.min.js"></script>
            <script type="text/javascript" src="../../qunit/qunit.js"></script>
            <script type="text/javascript" src="../../frameloader/frameloader.js"></script>
            <script type="text/javascript" src="./test-dom.js"></script>
        </head>
        <body>
            <h1 id="qunit-header">HTML5 Test Suite</h1>
            <h2 id="qunit-banner"></h2>
            <div id="qunit-testrunner-toolbar"></div>
            <h2 id="qunit-userAgent"></h2>
            <ol id="qunit-tests"></ol>
            <div id="qunit-fixture">test markup</div>
            <iframe height="600" width="1000" id="frame"></iframe>
        </body>
    </html>


### Sample QUnit JS File

    QUnit.config.reorder = false;
    base = [ "http://", window.location.host, "/ticket-monster/" ].join("");
    
    module('Ajax calls');
    
    test('Read events', function() {
        expect(7);
        stop();
        $.ajax({
            url : (base + "rest/events")
        }).done(function(data) {
            console.debug(data);
            equal(data.length, 19, "Received 19 events");
            event = data[7];
            notEqual(event.id, undefined, "Event is properly formed");
            notEqual(event.name, undefined, "Event is properly formed");
            notEqual(event.description, undefined, "Event is properly formed");
            notEqual(event.category, undefined, "Event is properly formed");
            notEqual(event.description, undefined, "Event is properly formed");
            notEqual(event.mediaItem, undefined, "Event is properly formed");
            start();
        })
    });

### Sample Test Case

     /*
      * JBoss, Home of Professional Open Source
      * Copyright 2013, Red Hat, Inc., and individual contributors
      * by the @authors tag. See the copyright.txt in the distribution for a
      * full listing of individual contributors.
      *
      * Licensed under the Apache License, Version 2.0 (the "License");
      * you may not use this file except in compliance with the License.
      * You may obtain a copy of the License at
      * http://www.apache.org/licenses/LICENSE-2.0
      * Unless required by applicable law or agreed to in writing, software
      * distributed under the License is distributed on an "AS IS" BASIS,
      * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      * See the License for the specific language governing permissions and
      * limitations under the License.
      */
     
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
         @Deployment()
         public static Archive<?> createDeployment() {
             return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT));
         }
     
         /**
          * Test the qunit-tests-dom.html file.
          */
         @QUnitTest("tests/ticketmonster/qunit-tests-dom.html")
         @InSequence(1)
         public void qunitDomTest() {
             // empty body - only the annotations are used
         }
     
         /**
          * Test the qunit-tests-ajax.html file.
          */
         @QUnitTest("tests/ticketmonster/qunit-tests-ajax.html")
         @InSequence(2)
         public void qunitAjaxTest() {
             // empty body - only the annotations are used
         }
     
         /**
          * Test the qunit-tests.html file.
          */
         @QUnitTest("tests/generic/qunitTest.html")
         @InSequence(3)
         public void qunitAssertionsTest() {
             // empty body - only the annotations are used
         }
     
     }
     
## Known Limitations
The run/execution time printed in the report  is not accurate.

## Documentation

* [Arquillian Guides](http://arquillian.org/guides/)
* [Create Deployable Archives with ShrinkWrap](http://arquillian.org/guides/shrinkwrap_introduction/)
