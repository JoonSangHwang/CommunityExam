package com.joonsang.example.CommunityExam.controller;


import com.joonsang.example.CommunityExam.entity.Account;
import com.joonsang.example.CommunityExam.entity.dto.AccountDto;
import com.joonsang.example.CommunityExam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ModelMapper modelMapper;


    /**
     * 회원 가입 페이지 이동
     */
    @GetMapping(value = "/signUpPage")
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
                .nickname(accountDto.getName())
                .email(accountDto.getEmail())
                .build();

        // 저장
        userService.createUser(account);

        return "redirect:/";
    }
}
