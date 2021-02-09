package com.joonsang.example.CommunityExam.board;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum BoardType {
    notice("공지사항"),
    free("자유게시판");

    private String value;


    BoardType(String value) {
        this.value = value;
    }
}
