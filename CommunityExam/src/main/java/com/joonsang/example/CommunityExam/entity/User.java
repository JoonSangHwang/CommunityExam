package com.joonsang.example.CommunityExam.entity;

//import com.joonsang.example.CommunityExam.user.Role;
import com.joonsang.example.CommunityExam.user.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Table
@NoArgsConstructor
public class User extends BaseTimeEntity implements Serializable {

        //== Property
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column private Long idx;

        @Column private String name;
        @Column private String password;
        @Column private String email;
        @Column private String picture;
        @Column private String pincipal;

        @Enumerated(EnumType.STRING)
        @Column private SocialType socialType;
        //== Property


        @Builder
        public User(String name, String password, String email, String pincipal, String picture, SocialType socialType) {
                this.name = name;
                this.password = password;
                this.email = email;
                this.pincipal = pincipal;
                this.picture = picture;
                this.socialType = socialType;
        }

        @Builder
        public User(String name, String email, String picture) {
                this.name = name;
                this.email = email;
                this.picture = picture;
        }

        public User update(String name, String picture) {
                this.name = name;
                this.picture = picture;
                return this;
        }
}
