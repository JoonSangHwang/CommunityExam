package com.joonsang.example.CommunityExam.entity.dto;

import com.joonsang.example.CommunityExam.entity.enumType.SocialType;
import lombok.Data;

@Data
public class UserDto {
    private String userId;
    private String name;
    private String password;
    private String email;
    private SocialType socialType;
}
