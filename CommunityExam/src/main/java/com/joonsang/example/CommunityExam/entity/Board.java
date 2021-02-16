package com.joonsang.example.CommunityExam.entity;

import com.joonsang.example.CommunityExam.board.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table
public class Board extends BaseEntity implements Serializable {

    //== Property
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Long idx;

    @Column private String title;
    @Column private String subTitle;
    @Column private String content;

    @Enumerated(EnumType.STRING)
    @Column private BoardType boardType;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    //== Property


    public Board() {
    }

    @Builder
    public Board(String title, String subTitle, String content, BoardType boardType, User user, String createdBy, String modifiedBy) {
        super(createdBy, modifiedBy);
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.user = user;
    }
}
