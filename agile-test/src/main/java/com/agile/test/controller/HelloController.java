package com.agile.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello test controller.
 *
 * @author Huang Z.Y.
 */
@RestController
public class HelloController {

    //    @SysLog("Say Hello")
    @GetMapping("/hello")
    public String sayHello(String name) {
        return new String("Hello, " + name);
    }

}
