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
package org.jboss.arquillian.qunit.junit.test;

import java.io.IOException;

import org.jboss.arquillian.qunit.api.model.DeploymentMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public final class DeploymentPackager {

    private static final String TEST_ARCHIVE = "test.war";

    private DeploymentPackager() {
    }

    private static class SingletonHolder {
        public static final DeploymentPackager INSTANCE = new DeploymentPackager();
    }

    public static DeploymentPackager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Archive<?> createPackage(TestSuite suite) throws IOException {
        final DeploymentMethod deploymentMethod = suite.getDeploymentMethod();
        final Object invocationResult = deploymentMethod != null ? deploymentMethod.getArchive() : null;
        final Archive<?> archive = invocationResult != null ? (Archive<?>) invocationResult : ShrinkWrap.create(
                WebArchive.class, TEST_ARCHIVE);

        archive.merge(
                ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory(suite.getQUnitResourcesPath()).as(GenericArchive.class), "/", Filters.includeAll());

        return archive;
    }
}
