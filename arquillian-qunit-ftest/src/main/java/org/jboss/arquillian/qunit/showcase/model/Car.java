/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.qunit.showcase.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@XmlRootElement
@Table(name = "CarStorage", uniqueConstraints = @UniqueConstraint(columnNames = "numberFrame"))
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    public Car() {
    }

    public Car(String numberFrame, String model, String color) {
        this.model = model;
        this.color = color;
        this.numberFrame = numberFrame;
    }

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    private String model;

    @NotNull
    @NotEmpty
    private String color;

    @NotNull
    @NotEmpty
    private String numberFrame;

    public String getNumberFrame() {
        return numberFrame;
    }

    public void setNumberFrame(String numberFrame) {
        this.numberFrame = numberFrame;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append(super.toString()).append(" [ id=").append(id).append(" model=").append(model)
            .append(" color=").append(color).append(" numberFrame=").append(numberFrame).append(" ]").toString();
    }
}
