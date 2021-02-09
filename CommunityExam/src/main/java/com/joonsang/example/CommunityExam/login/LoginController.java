package com.joonsang.example.CommunityExam.login;

import com.joonsang.example.CommunityExam.annotation.SocialUser;
import com.joonsang.example.CommunityExam.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@SocialUser User user) {
        return "redirect:/board/list";
    }
}
