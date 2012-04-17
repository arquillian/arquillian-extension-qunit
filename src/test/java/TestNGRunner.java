import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jboss.arquillian.qunit.testng.ClassGenerator;
import org.testng.annotations.Factory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class TestNGRunner {

    @Factory
    public Object[] factory() {
        List<Class<?>> classes = Arrays.asList(new ClassGenerator().getClasses());
        Collection<Object> tests = Collections2.transform(classes, new Function<Class<?>, Object>() {
            public Object apply(Class<?> from) {
                try {
                    return from.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
        return tests.toArray();
    }
}
