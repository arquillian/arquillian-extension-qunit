/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.arquillian.qunit.junit.model;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class QUnitAssertion {

    private boolean isFailed;

    private String source;

    public boolean isFailed() {
        return isFailed;
    }

    public QUnitAssertion setFailed(boolean isFailed) {
        this.isFailed = isFailed;
        return this;
    }

    public String getSource() {
        return source;
    }

    public QUnitAssertion setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append(super.toString()).append(" [isFailed=").append(this.isFailed).append(", source=")
                .append(this.source).append("]").toString();
    }

}
