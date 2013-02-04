package org.jboss.arquillian.qunit.junit;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;

public class QUnitWarpExtension implements RemoteLoadableExtension {

    public void register(ExtensionBuilder builder) {
        builder.observer(WarpQUnitObserver.class);
    }

}
