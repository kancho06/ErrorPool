package com.sparta.errorpool.config;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.article.Category;
import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.article.dto.ArticleCreateRequestDto;
import com.sparta.errorpool.comment.CommentRepository;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MockApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        User testUser = userRepository.findByEmail("rockintuna2@naver.com").orElse(null);
//        for (int categoryId = 1; categoryId < 4; categoryId++) {
//            for (int skillId = 1 ; skillId < 4; skillId++) {
//                for (int i = 0; i < 10; i++) {
//                    ArticleCreateRequestDto dto = new ArticleCreateRequestDto(
//                            Skill.getSkillById(skillId).getName()+" "+
//                                    Category.getCategoryById(categoryId) +" 게시글"+ i,
//                            null,
//                            "안녕하세요. 여기는 "+Skill.getSkillById(skillId).getName()+" "+
//                                    Category.getCategoryById(categoryId)+" 입니다용.",
//                            skillId,
//                            categoryId
//                    );
//                    Article article = new Article(dto, testUser);
//                    articleRepository.save(article);
//                }
//            }
//        }

    }

}