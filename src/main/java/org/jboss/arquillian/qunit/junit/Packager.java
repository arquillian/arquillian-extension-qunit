package org.jboss.arquillian.qunit.junit;

import java.lang.reflect.Method;

import org.jboss.arquillian.qunit.common.ReflectOperations;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Used to create the {@link Archive} which will be finally deployed on the AS.
 */
public class Packager {

    /**
     * The file name for the Archive which will contain the test files if application content does not exist
     */
    private static final String TEST_ARCHIVE_FILE_NAME = "test.war";

    /**
     * Creates the final {@link Archive} which will be deployed on the server. The Archive may be {@link EnterpriseArchive} or
     * {@link WebArchive} according the value of the Deployment annotation on the actual Test Case and will contain the QUnit
     * test cases and the application's content if any.
     * 
     * @param suite The {@link TestSuite}
     * @return {@link Archive}
     */
    public static final Archive<?> createPackage(TestSuite suite) {

        final Method deploymentMethod = suite.getDeploymentMethod();

        final Archive<?> archive = deploymentMethod != null ? (Archive<?>) ReflectOperations.invokeMethod(deploymentMethod,
            suite.getJavaClass()) : ShrinkWrap.create(WebArchive.class, TEST_ARCHIVE_FILE_NAME);

        // merge the tests and the required files to execute them
        archive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(suite.getWebRoot())
            .as(GenericArchive.class), "/", Filters.includeAll());

        return archive;

    }

}