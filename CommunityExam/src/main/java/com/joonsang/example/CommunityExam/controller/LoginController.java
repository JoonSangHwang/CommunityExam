package com.joonsang.example.CommunityExam.controller;

import com.joonsang.example.CommunityExam.annotation.SocialUser;
import com.joonsang.example.CommunityExam.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginPage")
    public String login() {
        return "login/loginPage";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@SocialUser User user) {
        System.out.println("OAuth 2.0 complete !!!!!!!");
        return "redirect:/board/list";
    }


}