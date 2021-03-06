package com.joonsang.example.CommunityExam.login;

import com.joonsang.example.CommunityExam.annotation.SocialUser;
import com.joonsang.example.CommunityExam.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@SocialUser User user) {
        System.out.println("OAuth 2.0 complete !!!!!!!");
        return "redirect:/board/list";
    }


    @GetMapping("/test")
    public User aa(@SocialUser User user) {
        return user;
    }
}
