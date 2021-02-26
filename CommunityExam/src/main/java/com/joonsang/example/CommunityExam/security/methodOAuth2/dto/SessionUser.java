package com.joonsang.example.CommunityExam.security.methodOAuth2.dto;

import com.joonsang.example.CommunityExam.entity.Account;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    // 인증 된 사용자 정보
    private String name;
    private String email;
    private String picture;

    public SessionUser(Account account) {
        this.name = account.getNickname();
        this.email = account.getEmail();
        this.picture = account.getPicture();
    }
}
