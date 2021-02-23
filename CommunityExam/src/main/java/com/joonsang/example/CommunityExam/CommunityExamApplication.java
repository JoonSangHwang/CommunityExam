package com.joonsang.example.CommunityExam;

import com.joonsang.example.CommunityExam.repository.BoardRepository;
import com.joonsang.example.CommunityExam.entity.enumType.BoardType;
import com.joonsang.example.CommunityExam.entity.Board;
import com.joonsang.example.CommunityExam.entity.User;
import com.joonsang.example.CommunityExam.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.IntStream;

@SpringBootApplication
@EnableJpaAuditing
public class CommunityExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityExamApplication.class, args);
	}







	/**
	 * 초기 기초 데이터
	 */
	@Bean
	public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository) {
		return (args) -> {
			User user = userRepository.save(User.builder()
					.name("abc")
					.password("test")
					.picture("test")
					.email("test@gmail.com")
					.build());



			IntStream.rangeClosed(1, 200).forEach(index ->
					boardRepository.save(Board.builder()
							.title("게시글"+index)
							.subTitle("순서"+index)
							.content("컨텐츠")
							.boardType(BoardType.free)
							.createdBy("Admin")
							.modifiedBy("Admin")
							.user(user).build())
			);
		};
	}
}
