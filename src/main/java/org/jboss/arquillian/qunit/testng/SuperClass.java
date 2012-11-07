package org.jboss.arquillian.qunit.testng;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.testng.Arquillian;
import org.openqa.selenium.WebDriver;

public class SuperClass extends Arquillian {

    protected static CallbackHandler handler;
    
    @Drone
    protected WebDriver browser;
}
