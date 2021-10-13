package com.sparta.errorpool.testdata;

import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.comment.CommentRepository;
import com.sparta.errorpool.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        IntStream.rangeClosed(1,15).forEach(i -> {
//
//                    User user = userRepository.findById(16L).orElse(null);
//                    Article article = articleRepository.findById(26L).orElse(null);
//
//                    Comment comment  = Comment.builder()
//                            .user(user)
//                            .article(article)
//                            .content("오류 번호 : ["+ i +"] 댓글 ")
//                            .build();
//            commentRepository.save(comment);
//                }
//        );
    }

}