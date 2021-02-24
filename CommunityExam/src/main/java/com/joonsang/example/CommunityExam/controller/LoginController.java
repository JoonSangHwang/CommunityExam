package com.joonsang.example.CommunityExam.controller;

import com.joonsang.example.CommunityExam.annotation.SocialUser;
import com.joonsang.example.CommunityExam.entity.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @GetMapping("/loginPage")
    public String login() {
        return "login/loginPage";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@SocialUser Account account) {
        System.out.println("OAuth 2.0 complete !!!!!!!");
        return "redirect:/board/list";
    }


//
//    @GetMapping(value="/loginForm")
//    public String login(@RequestParam(value = "error", required = false) String error,
//                        @RequestParam(value = "exception", required = false) String exception, Model model){
//        model.addAttribute("error",error);
//        model.addAttribute("exception",exception);
//        return "user/login/login";
//    }




    /**
     * 로그아웃
     * 1. <form> 태그를 사용하여 POST 요청
     * 2. <a> 태그를 사용하여 GET 요청 (v)
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        // 인증 객체 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // [세션 무효화] + [인증 객체 empty] + [SecurityContextHolder Clean]
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/loginPage";
    }



}
