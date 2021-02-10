package com.joonsang.example.CommunityExam.user;

import lombok.Getter;

@Getter
public enum SocialType {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    KAKAO("kakao");

    // 시큐리티에서 권한 코드는 항상 Prefix 가 "ROLE_" 이어야 한다.
    private final String ROLE_PREFIX = "ROLE_";
    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getRoleType() {
        return ROLE_PREFIX + name.toUpperCase();
    }
    public boolean isEquals(String authority) {
        return this.getRoleType().equals(authority);
    }
}
