package com.easylearning.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    @GetMapping("/getSample")
    public String sampleMethod(){

        return "Hi this is saddam hussain, I implemented jwt spring security project ";
    }

}
