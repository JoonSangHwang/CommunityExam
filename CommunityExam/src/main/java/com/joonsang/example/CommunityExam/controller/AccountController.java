package com.joonsang.example.CommunityExam.controller;


import com.joonsang.example.CommunityExam.entity.Account;
import com.joonsang.example.CommunityExam.entity.dto.AccountDto;
import com.joonsang.example.CommunityExam.entity.enumType.roleType;
import com.joonsang.example.CommunityExam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.joonsang.example.CommunityExam.entity.enumType.roleType.BRONZE;
import static com.joonsang.example.CommunityExam.entity.enumType.roleType.FACEBOOK;

@Controller
@RequiredArgsConstructor
public class AccountController {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ModelMapper modelMapper;


    /**
     * 회원 가입 페이지 이동
     */
    @RequestMapping(value = "/signUpPage", method= {RequestMethod.GET, RequestMethod.POST})
    public String signUp() {
        return "login/signUpPage";
    }


    /**
     * 회원 가입
     */
    @PostMapping("/signUp")
    public String createUser(AccountDto accountDto) {

        // 데이터 바인딩
        Account account = Account.builder()
                .userId(accountDto.getUserId())
                .password(passwordEncoder.encode(accountDto.getPassword()))        // 패스워드 암호화
                .nickname(accountDto.getNickname())
                .email(accountDto.getEmail())
                .roleType(BRONZE)
                .build();

        // 저장
        userService.createUser(account);

        return "redirect:/";
    }
}
