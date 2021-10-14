package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.ArticleCreateRequestDto;
import com.sparta.errorpool.security.WebSecurityConfig;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRoleEnum;
import com.sparta.errorpool.util.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( controllers = ArticleController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private ImageService imageService;

    private List<Article> articleList = new ArrayList<>();


    @BeforeEach
    private void beforeEach() {

        User user = new User("rockintuna@naver.com", "password", "rockintuna", UserRoleEnum.USER, Skill.SPRING);
        //given
        articleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 1", null, "test contet 1", 2, 2), user));
        articleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 2", null, "test contet 2", 2, 2), user));
        articleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 3", null, "test contet 3", 2, 2), user));
        articleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 4", null, "test contet 4", 2, 2), user));
        articleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 5", null, "test contet 5", 2, 2), user));

    }

    @Test
    @WithUserDetails
    public void getArticlesInSkillAndCategory() throws Exception {
        Page<Article> articlePages = new PageImpl<>(articleList);
        given(articleService.getArticlesInSkillAndCategory(2,3))
                .willReturn(articlePages);

        mvc.perform(get("/articles/skill/2/3"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    void getBestArticles() throws Exception {
        Page<Article> articlePages = new PageImpl<Article>(articleList);
        given(articleService.getBestArticleListOfAllSkill())
                .willReturn(articlePages);
        given(articleService.getBestArticleListIn(any(Skill.class)))
                .willReturn(articlePages);

        mvc.perform(get("/articles/recommended"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getArticleDetails() {
    }

    @Test
    void createArticle() {
    }

    @Test
    void updateArticle() {
    }

    @Test
    void deleteArticle() {
    }

    @Test
    void likeArticle() {
    }
}