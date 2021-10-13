package com.sparta.errorpool.mockdata;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.article.Category;
import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.comment.CommentRepository;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRepository;
import com.sparta.errorpool.user.UserRoleEnum;
import com.sparta.errorpool.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("============================");
        System.out.println("Mocking data");
        System.out.println("============================");

        User testUser = new User("tester@naver.com",
                passwordEncoder.encode("123"),
                "tester",
                UserRoleEnum.USER,
                Skill.SPRING);

        userRepository.save(testUser);
        mockArticles(testUser);
    }

    private void mockArticles(User testUser) {
        for (int i = 0; i < 10; i++) {
            articleRepository.save(createTestArticle(testUser, i));
        }
    }

    private Article createTestArticle(User testUser, Integer id) {
        Article article = new Article();
        article.setTitle("여기는 스프링 자유 게시판"+id);
        article.setContent("여기는 스프링 자유 게시판");
        article.setSkill(Skill.SPRING);
        article.setCategory(Category.FREE_BOARD);
        article.setUser(testUser);
        return article;
    }
}