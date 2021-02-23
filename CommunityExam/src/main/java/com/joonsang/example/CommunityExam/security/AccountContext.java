package com.joonsang.example.CommunityExam.security;

import com.joonsang.example.CommunityExam.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Spring Security 가 제공하는 User 객체를 상속받음 (UserDetails 타입을 반환)
 */
public class AccountContext extends org.springframework.security.core.userdetails.User {

    private final User account;

    public AccountContext(User account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getName(), account.getPassword(), authorities);
        this.account = account;
    }

    public User getAccount() {
        return account;
    }
}