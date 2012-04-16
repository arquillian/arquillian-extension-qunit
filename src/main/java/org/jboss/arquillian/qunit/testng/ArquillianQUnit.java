package org.jboss.arquillian.qunit.testng;


import org.jboss.arquillian.testng.Arquillian;
import org.testng.annotations.Listeners;

@Listeners(QUnitMethodAddingInterceptor.class)
public class ArquillianQUnit extends Arquillian {
}
