package com.joonsang.example.CommunityExam.entity.dto;

import com.joonsang.example.CommunityExam.entity.enumType.roleType;
import lombok.Data;

@Data
public class AccountDto {
    private String userId;
    private String name;
    private String password;
    private String email;
    private roleType roleType;
}
