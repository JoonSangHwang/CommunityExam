package com.joonsang.example.CommunityExam.exception;

public class UserIdNotFoundException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 아이디 입니다.";

    public UserIdNotFoundException() {
        super(MESSAGE);
    }
}