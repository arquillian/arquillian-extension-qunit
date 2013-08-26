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
package org.jboss.arquillian.qunit.showcase.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.qunit.showcase.data.CarRepository;
import org.jboss.arquillian.qunit.showcase.model.Car;

@Path("/carservice")
@RequestScoped
@Stateful
public class CarService {

    @Inject
    private CarRepository repository;

    @GET
    @Path("/cars")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Car> listAllCars() {
        return repository.findAllCars();
    }
}
