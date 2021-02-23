package com.joonsang.example.CommunityExam.service.impl;

import com.joonsang.example.CommunityExam.entity.User;
import com.joonsang.example.CommunityExam.repository.UserRepository;
import com.joonsang.example.CommunityExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("userService")     // 빈 이름
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }
}
