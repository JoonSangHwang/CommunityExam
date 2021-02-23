package com.joonsang.example.CommunityExam;

import com.joonsang.example.CommunityExam.entity.Account;
import com.joonsang.example.CommunityExam.repository.BoardRepository;
import com.joonsang.example.CommunityExam.entity.enumType.BoardType;
import com.joonsang.example.CommunityExam.entity.Board;
import com.joonsang.example.CommunityExam.repository.AccountRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaMappingTest {
    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    public void init() {
        Account account = accountRepository.save(Account.builder()
                .nickname("havi")
                .password("test")
                .email(email)
                .build());

        boardRepository.save(Board.builder()
                .title(boardTestTitle)
                .subTitle("서브 타이틀")
                .content("컨텐츠")
                .boardType(BoardType.free)
                .account(account).build());
    }

    @Test
    @DisplayName("제대로_생성_됐는지_테스트")
    public void confirm() {

        System.out.println("email :" + email);

        Account account = accountRepository.findByEmail(email);
        assertThat(account.getNickname(), is("havi"));
        assertThat(account.getPassword(), is("test"));
        assertThat(account.getEmail(), is(email));

        Board board = boardRepository.findByAccount(account);
        assertThat(board.getTitle(), is(boardTestTitle));
        assertThat(board.getSubTitle(), is("서브 타이틀"));
        assertThat(board.getContent(), is("컨텐츠"));
        assertThat(board.getBoardType(), is(BoardType.free));
    }
}
