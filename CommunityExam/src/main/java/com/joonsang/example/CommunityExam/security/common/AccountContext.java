package com.joonsang.example.CommunityExam.security.common;

import com.joonsang.example.CommunityExam.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Spring Security 가 제공하는 User 객체를 상속받음 (UserDetails 타입을 반환)
 */
public class AccountContext extends User {

    private final Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getNickname(), account.getPassword(), authorities);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}