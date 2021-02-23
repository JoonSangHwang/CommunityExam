package com.joonsang.example.CommunityExam.controller;


import com.joonsang.example.CommunityExam.entity.User;
import com.joonsang.example.CommunityExam.entity.dto.UserDto;
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
public class UserController {

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
    public String createUser(UserDto userDto) {

        // 데이터 바인딩
        User user = User.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))        // 패스워드 암호화
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

        // 저장
        userService.createUser(user);

        return "redirect:/";
    }
}
