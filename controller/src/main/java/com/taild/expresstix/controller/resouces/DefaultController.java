package com.taild.expresstix.controller.resouces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String defaultPage() {
        return "Welcome to ExpressTix!";
    }
}
