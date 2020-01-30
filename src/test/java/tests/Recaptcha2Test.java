package tests;

import base.DriverBase;
import org.junit.Before;
import org.junit.Test;
import pages.Recaptcha2Page;

import java.io.IOException;

public class Recaptcha2Test extends DriverBase {
    private Recaptcha2Page recaptcha2Page;

    @Before
    public void init() {
        recaptcha2Page = new Recaptcha2Page(driver);
    }

    @Test
    public void googleRecaptcha2DemoTest() throws InterruptedException, IOException {
        recaptcha2Page
                .setResponseForRecaptchaWithSelenium();
    }

}
