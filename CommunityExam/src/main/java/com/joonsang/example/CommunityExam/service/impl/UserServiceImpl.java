package com.joonsang.example.CommunityExam.service.impl;

import com.joonsang.example.CommunityExam.entity.Account;
import com.joonsang.example.CommunityExam.repository.AccountRepository;
import com.joonsang.example.CommunityExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("userService")     // 빈 이름
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public void createUser(Account account) {
        accountRepository.save(account);
    }
}
