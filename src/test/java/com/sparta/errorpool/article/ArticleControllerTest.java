package com.sparta.errorpool.article;

import com.sparta.errorpool.util.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private ImageService imageService;

    @Test
    void getArticlesInSkillAndCategory() throws Exception {
        mvc.perform(get("/articles/skill/{skill_id}/{category_id}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getBestArticles() throws Exception {
        mvc.perform(get("/articles/recommended"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getArticleDetails() throws Exception {
        mvc.perform(get("/articles/{article_id}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createArticle() throws Exception {
        mvc.perform(post("/articles"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateArticle() throws Exception {
        mvc.perform(put("/articles/{article_id}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteArticle() throws Exception {
        mvc.perform(delete("/articles/{article_id}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void likeArticle() throws Exception {
        mvc.perform(put("/articles/{article_id}/like"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}