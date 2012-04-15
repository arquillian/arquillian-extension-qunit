import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.qunit.DefaultModule;
import org.jboss.arquillian.qunit.DefaultTest;
import org.jboss.arquillian.qunit.Module;
import org.jboss.arquillian.qunit.ModuleReader;
import org.jboss.arquillian.qunit.Test;


public class TestModuleReader implements ModuleReader {
    
    public List<Module> readModules(URL url) {
        List<? extends Test> module1Tests = Arrays.asList(new DefaultTest("test1"), new DefaultTest("test2"));
        List<? extends Test> module2Tests = Arrays.asList(new DefaultTest("test3"), new DefaultTest("test4"));
        
        Module module1 = new DefaultModule("module1", module1Tests);
        Module module2 = new DefaultModule("module2", module2Tests);
        
        return Arrays.asList(module1, module2);
    }
}
