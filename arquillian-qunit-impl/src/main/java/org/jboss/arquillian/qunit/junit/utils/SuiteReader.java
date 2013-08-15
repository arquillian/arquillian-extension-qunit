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
package org.jboss.arquillian.qunit.junit.utils;

import static org.jboss.arquillian.qunit.junit.utils.FileOperations.readFile;
import static org.jboss.arquillian.qunit.junit.utils.FileOperations.writeToFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sourceforge.htmlunit.corejs.javascript.ConsString;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.qunit.api.model.TestMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.model.QUnitTestImpl;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class SuiteReader {

    private static final String qunitReaderJS = "(function(a){if(a.QUnit!==undefined){a.qunitTestArr=a.qunitTestArr||[];if(!String.prototype.trim){String.prototype.trim=function(){return this.replace(/^\\s+|\\s+$/g,\"\")}}a.test=function(b,d,e,c){a.qunitTestArr.push(((QUnit.config.currentModule&&String(QUnit.config.currentModule).trim()!==\"\")?QUnit.config.currentModule:\"\")+\":\"+((b&&b.trim()!==\"\")?b:\"\"))};a.asyncTest=function(b,c,d){a.qunitTestArr.push(((QUnit.config.currentModule&&String(QUnit.config.currentModule).trim()!==\"\")?QUnit.config.currentModule:\"\")+\":\"+((b&&b.trim()!==\"\")?b:\"\"))};a.QUnit.test=function(b,d,e,c){a.qunitTestArr.push(((QUnit.config.currentModule&&String(QUnit.config.currentModule).trim()!==\"\")?QUnit.config.currentModule:\"\")+\":\"+((b&&b.trim()!==\"\")?b:\"\"))};a.QUnit.asyncTest=function(b,c,d){a.qunitTestArr.push(((QUnit.config.currentModule&&String(QUnit.config.currentModule).trim()!==\"\")?QUnit.config.currentModule:\"\")+\":\"+((b&&b.trim()!==\"\")?b:\"\"))}}})(this); \n \n";

    private static final String tmpFolder = "target/qunit-temp";

    public static HashMap<String, List<String>> readQUnitTests(Archive<?> archive, TestSuite suite) {
        injectQUnitSuiteReader(archive);
        return readQUnitSuite(archive, suite);
    }

    private static HashMap<String, List<String>> readQUnitSuite(Archive<?> archive, TestSuite suite) {

        HashMap<String, List<String>> qunitFileName_TestsHM = new HashMap<String, List<String>>();

        try {
            final TestMethod[] qunitTestMethods = suite.getTestMethods();
            if (!ArrayUtils.isEmpty(qunitTestMethods)) {

                HtmlUnitDriver driver = new HtmlUnitDriver(true);

                //int testIndex = 0;
                for (TestMethod method : qunitTestMethods) {
                    if (!StringUtils.isEmpty(method.getQunitTestFile())) {

                        qunitFileName_TestsHM.put(method.getQunitTestFile(), new ArrayList<String>());

                        final String qunitTestFilePath = tmpFolder + "/" + archive.getName() + "/" + method.getQunitTestFile();

                        @SuppressWarnings("deprecation")
                        URL url = new File(qunitTestFilePath).toURL();

                        driver.get(url.toExternalForm());

                        @SuppressWarnings("unchecked")
                        List<ConsString> qunitTestList = (List<ConsString>) driver.executeScript("return window.qunitTestArr");

                        if (!CollectionUtils.isEmpty(qunitTestList)) {

                            for (ConsString moduleConsString : qunitTestList) {

                                final String[] parts = moduleConsString.toString().split(":");

                                QUnitTestImpl test = new QUnitTestImpl().setModuleName(parts[0]).setName(parts[1]);
                                //test.setIndex(testIndex++);

                                qunitFileName_TestsHM.get(method.getQunitTestFile()).add(test.getDescriptionName());
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return qunitFileName_TestsHM;
    }

    private static void injectQUnitSuiteReader(Archive<?> archive) {
        try {
            File destinationDir = new File(tmpFolder);
            destinationDir.mkdir();
            archive.as(ExplodedExporter.class).exportExploded(destinationDir);

            for (Entry<ArchivePath, Node> entry : archive.getContent(Filters.include("([^\\s]+(\\.(js))$)")).entrySet()) {
                final String jsFilePath = tmpFolder + "/" + archive.getName() + entry.getValue();
                File jsFile = new File(jsFilePath);
                final String initialJsContent = readFile(jsFilePath);
                jsFile.delete();
                final String modifiedJsContent = new StringBuilder().append(qunitReaderJS).append(initialJsContent).toString();
                jsFile.createNewFile();
                writeToFile(jsFile, modifiedJsContent);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

}
