package com.joonsang.example.CommunityExam.entity;

//import com.joonsang.example.CommunityExam.user.Role;
import com.joonsang.example.CommunityExam.entity.enumType.roleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Table
@NoArgsConstructor
public class Account extends BaseTimeEntity implements Serializable {

        //== Property
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column private Long idx;

        @Column private String userId;
        @Column private String nickname;
        @Column private String password;
        @Column private String email;
        @Column private String picture;

        @Enumerated(EnumType.STRING)
        @Column private roleType roleType;
        //== Property


        @Builder
        public Account(String userId, String nickname, String password, String email, String picture, roleType roleType) {
                this.userId = userId;
                this.nickname = nickname;
                this.password = password;
                this.email = email;
                this.picture = picture;
                this.roleType = roleType;
        }

        @Builder
        public Account(String nickname, String email, String picture) {
                this.nickname = nickname;
                this.email = email;
                this.picture = picture;
        }

        public Account update(String nickname, String picture) {
                this.nickname = nickname;
                this.picture = picture;
                return this;
        }
}
