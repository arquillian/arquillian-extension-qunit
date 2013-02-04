package org.jboss.arquillian.qunit.junit;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.warp.spi.WarpDeploymentEnrichmentExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class WarpExtension implements WarpDeploymentEnrichmentExtension {

    public void enrichWebArchive(WebArchive archive) {
        archive.addAsServiceProvider(RemoteLoadableExtension.class, QUnitWarpExtension.class);
    }

    public JavaArchive getEnrichmentLibrary() {
        return null;
    }

}
