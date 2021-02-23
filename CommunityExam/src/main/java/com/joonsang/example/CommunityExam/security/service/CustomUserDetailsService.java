package com.joonsang.example.CommunityExam.security.service;

import com.joonsang.example.CommunityExam.entity.Account;
import com.joonsang.example.CommunityExam.repository.AccountRepository;
import com.joonsang.example.CommunityExam.security.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 계정을 DB 에서 조회하여 반환
 * -> User 타입으로 반환하기 위해, UserDetailsService 을 구현함
 *
 * UserDtails 는 스프링시큐리티에서 사용자 인증정보를 담는 클래스타입으로 만들어 놓은 것일뿐 이것에 대한 강제조항은 없습니다
 *
 * 일반적으로 인증처리 시 사용자정보를 얻는데 사용되는 UserDetailsService 클래스가 최종적으로 리턴하는 타입이 UserDetails 이기 때문에 이 클래스를 구현하는 것일뿐 UserDetailsService 를 사용하지 않고 자체적으로 별도의 서비스를 사용해서 인증사용자 정보를 얻도록 한다면 굳이 UserDetails 를 구현할 필요도 없습니다
 *
 * 그리고 Account 엔터티는 일반객체가 아닌 ORM 객체이기 때문에 여기에 UserDetails 를 구현하는 것은 맞지 않습니다
 *
 * 그래서 AccountContext 를 만들어 UserDetails 를 구현하고 여기에 Account 를 저장한다음  필요시에 Account 를 꺼내어 참조하게끔 한다는 의미입니다
 *
 * 그리고 번거로울 수 있지만 Account 엔터티도 AccountDto 같은 객체로 변환해서 사용하는 것이 더 좋은 구조일 수 있습니다
 *
 * 즉 AccountContext > AccountDto <-Account
 *
 * 처럼 되겠네요
 *
 * 중요한 것은 스프링시큐리티에서 인증사용자 정보를 어떻게 활용하고 있는지의 처리방식과 구조를 명확하게 이해하는 것이 무엇보다 중요합니다
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * 로그인 요청
     * - ID 를 DB 에서 조회 후 권한 생성 및 유저 정보 반환
     * - AccountContext 클래스에서 UserDetails 객체를 반환 하도록 함
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        // 권한 정보 생성
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("BRONZE"));

        /**
         * AccountContext : 유저 정보 (UserDetails 인터페이스를 사용하여 시큐리티가 구현 된 User 객체를 사용할 수 있다.)
         *
         * 참고로 반드시 AccountContext 를 만들 필요는 없습니다.
         * User 클래스를 UserDetails 타입으로 구현해서 사용해도 된다.
         * 근데 User 는 Entity 클래스로서 일반 객체와 분리해서 사용하는 것이 좀 더 좋은 구조이다.
         * 그래서 AccountContext 안에 User 를 담아놓고 필요시 꺼내어 쓰기 위한 용도로 만들었다.
         */
        AccountContext accountContext = new AccountContext(account, roles);

        return accountContext;
    }
}