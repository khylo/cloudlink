package com.khylo.cloudlink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// Example of controller.. Not used as it overrides RepositoryRestResource
@Controller
public class HelloController {

    @GetMapping(value="/hello")
    public @ResponseBody String sayHello(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        //model.addAttribute("user", name);
        return "Hello "+name;
    }
}
