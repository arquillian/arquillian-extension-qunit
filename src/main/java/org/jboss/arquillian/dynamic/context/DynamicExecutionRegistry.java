package org.jboss.arquillian.dynamic.context;

import java.util.List;

public class DynamicExecutionRegistry {

    private List<DynamicExecution<?>> executions;

    public void add(DynamicExecution<?> execution) {
        executions.add(execution);
    }

    public List<DynamicExecution<?>> getExecutions() {
        return executions;
    }
}
