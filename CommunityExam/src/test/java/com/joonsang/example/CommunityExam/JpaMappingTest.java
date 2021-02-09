package com.joonsang.example.CommunityExam;

import com.joonsang.example.CommunityExam.board.BoardRepository;
import com.joonsang.example.CommunityExam.board.BoardType;
import com.joonsang.example.CommunityExam.entity.Board;
import com.joonsang.example.CommunityExam.entity.User;
import com.joonsang.example.CommunityExam.user.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaMappingTest {
    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    public void init() {
        User user = userRepository.save(User.builder()
                .name("havi")
                .password("test")
                .email(email)
                .createdDate(LocalDateTime.now())
                .build());

        boardRepository.save(Board.builder()
                .title(boardTestTitle)
                .subTitle("서브 타이틀")
                .content("컨텐츠")
                .boardType(BoardType.free)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .user(user).build());
    }

    @Test
    @DisplayName("제대로_생성_됐는지_테스트")
    public void confirm() {

        System.out.println("email :" + email);

        User user = userRepository.findByEmail(email);
        assertThat(user.getName(), is("havi"));
        assertThat(user.getPassword(), is("test"));
        assertThat(user.getEmail(), is(email));

        Board board = boardRepository.findByUser(user);
        assertThat(board.getTitle(), is(boardTestTitle));
        assertThat(board.getSubTitle(), is("서브 타이틀"));
        assertThat(board.getContent(), is("컨텐츠"));
        assertThat(board.getBoardType(), is(BoardType.free));
    }
}
