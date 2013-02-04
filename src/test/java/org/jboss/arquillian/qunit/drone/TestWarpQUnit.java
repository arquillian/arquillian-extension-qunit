package org.jboss.arquillian.qunit.drone;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.jboss.arquillian.qunit.junit.QUnitRunner;
import org.jboss.arquillian.qunit.junit.QUnitTest;
import org.jboss.arquillian.qunit.junit.WebRoot;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.junit.runner.RunWith;

@WarpTest
@RunWith(QUnitRunner.class)
@WebRoot("/mnt/workspace/workspaces/arquillian/arquillian-qunit/src/test/resources/warp-test")
@QUnitTest("warp-test.html")
public class TestWarpQUnit {

    @Named("foo")
    public class VerifyAjaxRequest extends Inspection {

        @ArquillianResource
        HttpServletRequest request;

        @BeforeServlet
        public void testAjaxRequest() {
            System.out.println(request.getRequestURI());
        }
    }
}
