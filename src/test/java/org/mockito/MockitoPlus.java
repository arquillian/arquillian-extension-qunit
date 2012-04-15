package org.mockito;

public class MockitoPlus extends Mockito {

    public static <T> T spy(T object, Class<?> extraInterfaces) {
        return MOCKITO_CORE.mock((Class<T>) object.getClass(),
                withSettings().spiedInstance(object).defaultAnswer(CALLS_REAL_METHODS).extraInterfaces(extraInterfaces));
    }

}
