package org.jboss.arquillian.qunit.testng;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map.Entry;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TestPackager {
    
    public static void scan() throws IOException {
        Path path = FileSystems.getDefault().getPath("/home/lfryc/workspaces/arquillian/qunit");
        
        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                
                return FileVisitResult.CONTINUE;
            }
        };
        
        Files.walkFileTree(path, visitor);
    }
    
    public static void scan2() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war");
        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory("/home/lfryc/workspaces/arquillian/qunit").as(GenericArchive.class),
            "/", Filters.includeAll());
        
//        System.out.println(war.toString(true));
        File file = new File("src/test/resources/qunit-reader/suite-reader.js"); 
        
        for (Entry<ArchivePath, Node> entry : war.getContent(Filters.include(".*\\/qunit.js")).entrySet()) {
            war.delete(entry.getKey());
            war.addAsWebResource(file, entry.getKey());
        }
        
//        System.out.println(war.toString(true));
        
        File destinationDir = new File("target/qunit-temp");
        destinationDir.mkdir();
        war.as(ExplodedExporter.class).exportExploded(destinationDir);
    }
    
}
