package org.jboss.arquillian.qunit;

import java.net.URL;
import java.util.List;

public interface ModuleReader {

    List<Module> readModules(URL url);
}
