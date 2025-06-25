package com.ipaam.logging;

import com.ipaam.logging.annotation.LogMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @LogMethod
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}