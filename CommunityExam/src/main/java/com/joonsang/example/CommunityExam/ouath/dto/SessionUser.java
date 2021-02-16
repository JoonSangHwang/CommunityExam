package com.joonsang.example.CommunityExam.ouath.dto;

import com.joonsang.example.CommunityExam.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    // 인증 된 사용자 정보
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
