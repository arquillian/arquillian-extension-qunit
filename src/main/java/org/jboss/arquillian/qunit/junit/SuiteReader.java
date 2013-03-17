package org.jboss.arquillian.qunit.junit;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.qunit.testng.TestPackager;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SuiteReader {

    public static TestSuite read(Class<?> suiteClass) throws MalformedURLException {

        final TestSuite suite = new TestSuite(suiteClass);
        final WebArchive war = TestPackager.scan2(suite, true);
        final File destinationDir = new File("target/qunit-temp");
        FileUtils.deleteQuietly(destinationDir);
        destinationDir.mkdir();
        war.as(ExplodedExporter.class).exportExploded(destinationDir);

        final HtmlUnitDriver driver = new HtmlUnitDriver(true);

        final LinkedHashSet<String> qunitTestsHS = suite.getSortedUniqueQunitTestValues();
        if (!CollectionUtils.isEmpty(qunitTestsHS)) {
            final Iterator<String> qunitTestIter = qunitTestsHS.iterator();
            while (qunitTestIter.hasNext()) {
                final String qunitTestVal = qunitTestIter.next();
                if (!StringUtils.isEmpty(qunitTestVal)) {

                    final File file = new File("target/qunit-temp/test.war/" + qunitTestVal);
                    @SuppressWarnings("deprecation")
                    final URL url = file.toURL();
                    final TestFile testFile = suite.getOrAddFile(file.getAbsolutePath());
                    final UniqueName uniqueTestName = new UniqueName();
                    int testNumber = 0;

                    driver.get(url.toExternalForm());

                    @SuppressWarnings("unchecked")
                    List<String> texts = (List<String>) driver.executeScript("return window.tests");
                    if (!CollectionUtils.isEmpty(texts)) {
                        for (Object textObj : texts) {
                            final String text = textObj.toString();
                            final int colon = text.indexOf(':');
                            final String moduleName = text.substring(0, colon).trim();
                            String testName = text.substring(colon + 1).trim();
                            testName = uniqueTestName.getName(moduleName, testName);
                            final TestModule module = testFile.getOrAddModule(moduleName);
                            module.addFunction(testName, testNumber);
                            testNumber += 1;
                        }
                    }
                }
            }
        }

        return suite;
    }

}
