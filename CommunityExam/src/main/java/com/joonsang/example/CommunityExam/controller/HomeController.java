package com.joonsang.example.CommunityExam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/mainPage")
    public String main() {
        return "main/mainPage";
    }

}