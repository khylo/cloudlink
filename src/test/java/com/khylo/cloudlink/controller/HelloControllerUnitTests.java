package com.khylo.cloudlink.controller;

import org.junit.Test;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.Assert.assertEquals;

public class HelloControllerUnitTests {
    @Test
    public void testSayHello() {
        HelloController controller = new HelloController();
        /*Model model = new BindingAwareModelMap();
        String name = controller.sayHello("Dolly", model);
        assertEquals("Dolly", model.asMap().get("user"));
        assertEquals("hello", name);   // Note "hello", not "Dolly"
        */
        String name = controller.sayHello("Dolly");
        assertEquals("Hello Dolly", name);
    }
}
