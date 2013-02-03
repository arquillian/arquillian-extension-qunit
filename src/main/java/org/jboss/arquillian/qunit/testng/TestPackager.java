package org.jboss.arquillian.qunit.testng;

import java.io.File;
import java.util.Map.Entry;

import org.jboss.arquillian.qunit.junit.TestSuite;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TestPackager {

    public static WebArchive scan2(TestSuite suite, boolean replace) {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war");
        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory(suite.getWebRoot()).as(GenericArchive.class),
            "/", Filters.includeAll());

        if (replace) {
            File file = new File("src/test/resources/qunit-reader/suite-reader.js");
            for (Entry<ArchivePath, Node> entry : war.getContent(Filters.include(".*\\/qunit.js")).entrySet()) {
                war.delete(entry.getKey());
                war.addAsWebResource(file, entry.getKey());
            }
        }

        File destinationDir = new File("target/qunit-temp");
        destinationDir.mkdir();
        war.as(ExplodedExporter.class).exportExploded(destinationDir);

        return war;
    }
}
