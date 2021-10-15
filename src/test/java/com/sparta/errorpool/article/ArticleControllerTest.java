package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.ArticleCreateRequestDto;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.exception.UnauthenticatedException;
import com.sparta.errorpool.security.WebSecurityConfig;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRoleEnum;
import com.sparta.errorpool.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest( controllers = ArticleController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;


    @MockBean
    private ArticleService articleService;
    @MockBean
    private UserService userService;

    private List<Article> mockArticleList = new ArrayList<>();
    User testUser;

    @BeforeEach
    private void beforeEach() {

        testUser = new User("rockintuna@naver.com", "password", "rockintuna", UserRoleEnum.USER, Skill.SPRING);
        //given
        mockArticleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 1", null, "test content 1", 2, 2), testUser));
        mockArticleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 2", null, "test content 2", 2, 2), testUser));
        mockArticleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 3", null, "test content 3", 2, 2), testUser));
        mockArticleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 4", null, "test content 4", 2, 2), testUser));
        mockArticleList.add(Article.of(
                new ArticleCreateRequestDto
                        ("test title 5", null, "test content 5", 2, 2), testUser));
    }

    @Nested
    @DisplayName("Post 요청")
    class Post {
        @Nested
        @DisplayName("Post 요청 성공")
        class PostSuccess {
            @Test
            @DisplayName("POST /articles")
            @WithUserDetails
            void createArticleWithNoImage() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(post("/articles")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("title", "test")
                                .param("content", "test content")
                                .param("skillId", "1")
                                .param("categoryId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.successYn").value(true))
                        .andExpect(jsonPath("$.statusCode").value(200));

                verify(articleService).createArticle(any(), eq(testUser));
            }
        }
        @Nested
        @DisplayName("Post 요청 실패")
        class PostFail {
            @Test
            @DisplayName("제목 없음")
            @WithUserDetails
            void createArticleWithNoTitle() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(post("/articles")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("content", "test content")
                                .param("skillId", "1")
                                .param("categoryId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("내용 없음")
            @WithUserDetails
            void createArticleWithNoContent() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(post("/articles")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("title", "test")
                                .param("skillId", "1")
                                .param("categoryId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("주특기 번호 없음")
            @WithUserDetails
            void createArticleWithNoSkillId() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(post("/articles")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("title", "test")
                                .param("content", "test content")
                                .param("categoryId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("카테고리 번호 없음")
            @WithUserDetails
            void createArticleWithNoCategoryId() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(post("/articles")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("title", "test")
                                .param("content", "test content")
                                .param("skillId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("로그인 안함")
            @WithUserDetails
            void createArticleNoLogin() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willThrow(UnauthenticatedException.class);

                mvc.perform(post("/articles")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("title", "test")
                                .param("content", "test content")
                                .param("skillId", "1")
                                .param("categoryId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(401));

                verify(articleService, never()).createArticle(any(), any());
            }
        }
    }

    @Nested
    @DisplayName("Get 요청")
    class Get {
        @Nested
        @DisplayName("Get 요청 성공")
        class GetSuccess {
            @Test
            @WithUserDetails
            @DisplayName("항목 별 게시글 조회")
            public void getArticlesInSkillAndCategory() throws Exception {
                Page<Article> articlePages = new PageImpl<>(mockArticleList);
                given(articleService.getArticlesInSkillAndCategory(1,2,3))
                        .willReturn(articlePages);

                mvc.perform(get("/articles/skill/2/3")
                                .param("page", "1"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(true))
                        .andExpect(jsonPath("$.statusCode")
                                .value(200))
                        .andExpect(jsonPath("$.data.page")
                                .value(1))
                        .andExpect(jsonPath("$.data.articleList[0].title")
                                .value(mockArticleList.get(0).getTitle()))
                        .andExpect(jsonPath("$.data.articleList[0].content")
                                .value(mockArticleList.get(0).getContent()))
                        .andExpect(jsonPath("$.data.articleList[0].skillId")
                                .value(mockArticleList.get(0).getSkill().getNum()))
                        .andExpect(jsonPath("$.data.articleList[0].categoryId")
                                .value(mockArticleList.get(0).getCategory().getNum()));

                verify(articleService).getArticlesInSkillAndCategory(1, 2,3);
            }

            @Test
            @WithUserDetails
            @DisplayName("메인페이지 게시글 조회")
            void getBestArticles() throws Exception {
                Page<Article> articlePages = new PageImpl<>(mockArticleList);
                given(articleService.getBestArticleListOfAllSkill())
                        .willReturn(articlePages);
                given(articleService.getBestArticleListIn(any(Skill.class)))
                        .willReturn(articlePages);

                mvc.perform(get("/articles/recommended"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(true))
                        .andExpect(jsonPath("$.statusCode")
                                .value(200))
                        .andExpect(jsonPath("$.data.top5Articles[0].title")
                                .value(mockArticleList.get(0).getTitle()))
                        .andExpect(jsonPath("$.data.top5Articles[0].content")
                                .value(mockArticleList.get(0).getContent()))
                        .andExpect(jsonPath("$.data.top5Articles[0].skillId")
                                .value(mockArticleList.get(0).getSkill().getNum()))
                        .andExpect(jsonPath("$.data.top5Articles[0].categoryId")
                                .value(mockArticleList.get(0).getCategory().getNum()))
                        .andExpect(jsonPath("$.data.top5ReactArticleList[0].title")
                                .value(mockArticleList.get(0).getTitle()))
                        .andExpect(jsonPath("$.data.top5ReactArticleList[0].content")
                                .value(mockArticleList.get(0).getContent()))
                        .andExpect(jsonPath("$.data.top5ReactArticleList[0].skillId")
                                .value(mockArticleList.get(0).getSkill().getNum()))
                        .andExpect(jsonPath("$.data.top5ReactArticleList[0].categoryId")
                                .value(mockArticleList.get(0).getCategory().getNum()))
                        .andExpect(jsonPath("$.data.top5NodeJsArticleList[0].title")
                                .value(mockArticleList.get(0).getTitle()))
                        .andExpect(jsonPath("$.data.top5NodeJsArticleList[0].content")
                                .value(mockArticleList.get(0).getContent()))
                        .andExpect(jsonPath("$.data.top5NodeJsArticleList[0].skillId")
                                .value(mockArticleList.get(0).getSkill().getNum()))
                        .andExpect(jsonPath("$.data.top5NodeJsArticleList[0].categoryId")
                                .value(mockArticleList.get(0).getCategory().getNum()))
                        .andExpect(jsonPath("$.data.top5SpringArticleList[0].title")
                                .value(mockArticleList.get(0).getTitle()))
                        .andExpect(jsonPath("$.data.top5SpringArticleList[0].content")
                                .value(mockArticleList.get(0).getContent()))
                        .andExpect(jsonPath("$.data.top5SpringArticleList[0].skillId")
                                .value(mockArticleList.get(0).getSkill().getNum()))
                        .andExpect(jsonPath("$.data.top5SpringArticleList[0].categoryId")
                                .value(mockArticleList.get(0).getCategory().getNum()));

                verify(articleService).getBestArticleListOfAllSkill();
                verify(articleService).getBestArticleListIn(Skill.REACT);
                verify(articleService).getBestArticleListIn(Skill.SPRING);
                verify(articleService).getBestArticleListIn(Skill.NODEJS);
            }

            @Test
            @WithUserDetails
            @DisplayName("게시글 상세 조회")
            void getArticleDetails() throws Exception {
                Article article = mockArticleList.get(0);
                given(articleService.getArticleAndUpViewCountById(1L))
                        .willReturn(article);

                mvc.perform(get("/articles/1"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(true))
                        .andExpect(jsonPath("$.statusCode")
                                .value(200))
                        .andExpect(jsonPath("$.data.title")
                                .value(article.getTitle()))
                        .andExpect(jsonPath("$.data.content")
                                .value(article.getContent()))
                        .andExpect(jsonPath("$.data.skillId")
                                .value(article.getSkill().getNum()))
                        .andExpect(jsonPath("$.data.categoryId")
                                .value(article.getCategory().getNum()));

                verify(articleService).getArticleAndUpViewCountById(1L);
            }
        }
        @Nested
        @DisplayName("Get 요청 실패")
        class GetFail {
            @Test
            @WithUserDetails
            @DisplayName("없는 게시글 조회")
            void getArticleDetailsNotExist() throws Exception {
                Article article = mockArticleList.get(0);
                given(articleService.getArticleAndUpViewCountById(1L))
                        .willThrow(ArticleNotFoundException.class);

                mvc.perform(get("/articles/1"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(404));

                verify(articleService).getArticleAndUpViewCountById(1L);
            }
        }
    }

    @Nested
    @DisplayName("Put 요청")
    class Put {
        @Nested
        @DisplayName("Put 요청 성공")
        class PutSuccess {
            @Test
            @DisplayName("PUT /articles/{article_id}")
            @WithUserDetails
            void updateArticle() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(put("/articles/1")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("title", "test")
                                .param("content", "test content")
                                .param("skillId", "1")
                                .param("categoryId", "1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.successYn").value(true))
                        .andExpect(jsonPath("$.statusCode").value(200));

                verify(articleService).updateArticle(eq(1L), any(), eq(testUser));
            }

        }
        @Nested
        @DisplayName("Put 요청 실패")
        class PutFail {
            @Test
            @DisplayName("없는 게시글 수정")
            @WithUserDetails
            void updateArticleNotExist() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);
                willThrow(ArticleNotFoundException.class)
                        .given(articleService)
                        .updateArticle(eq(1L), any(), eq(testUser));

                mvc.perform(put("/articles/1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(404));

                verify(articleService).updateArticle(eq(1L), any(), eq(testUser));
            }

            @Test
            @DisplayName("권한 없음")
            @WithUserDetails
            void updateArticleNoPermission() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);
                willThrow(AccessDeniedException.class)
                        .given(articleService)
                        .updateArticle(eq(1L), any(), eq(testUser));

                mvc.perform(put("/articles/1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(403));

                verify(articleService).updateArticle(eq(1L), any(), eq(testUser));
            }
            @Test
            @DisplayName("로그인 안함")
            @WithUserDetails
            void updateArticleNoLogin() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willThrow(UnauthenticatedException.class);

                mvc.perform(put("/articles/1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(401));

                verify(articleService,never()).updateArticle(any(), any(), any());
            }
        }
    }

    @Nested
    @DisplayName("Delete 요청")
    class Delete {
        @Nested
        @DisplayName("Delete 요청 성공")
        class DeleteSuccess {
            @Test
            @WithUserDetails
            @DisplayName("DELETE /articles")
            void deleteArticle() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(delete("/articles/1")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.successYn").value(true))
                        .andExpect(jsonPath("$.statusCode").value(200));

                verify(articleService).deleteArticle(1L ,testUser);
            }
        }
        @Nested
        @DisplayName("Delete 요청 실패")
        class DeleteFail {
            @Test
            @DisplayName("없는 게시글 삭제")
            @WithUserDetails
            void deleteArticleNotExist() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);
                willThrow(ArticleNotFoundException.class)
                        .given(articleService)
                        .deleteArticle(1L, testUser);

                mvc.perform(delete("/articles/1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(404));

                verify(articleService).deleteArticle(1L ,testUser);
            }

            @Test
            @DisplayName("권한 없음")
            @WithUserDetails
            void deleteArticleNoPermission() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);
                willThrow(AccessDeniedException.class)
                        .given(articleService)
                        .deleteArticle(1L, testUser);

                mvc.perform(delete("/articles/1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(403));

                verify(articleService).deleteArticle(1L ,testUser);
            }

            @Test
            @DisplayName("로그인 안함")
            @WithUserDetails
            void deleteArticleNoLogin() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willThrow(UnauthenticatedException.class);

                mvc.perform(delete("/articles/1")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(401));

                verify(articleService,never()).deleteArticle(any() ,any());
            }
        }
    }


    @Nested
    @DisplayName("Like 요청")
    class Like {
        @Nested
        @DisplayName("Like 요청 성공")
        class LikeSuccess {
            @Test
            @DisplayName("PUT /articles/{article_id}/like")
            @WithUserDetails
            void likeArticle() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);

                mvc.perform(post("/articles/1/like")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(true))
                        .andExpect(jsonPath("$.statusCode")
                                .value(200));

                verify(articleService).likeArticle(1L ,testUser);
            }
        }
        @Nested
        @DisplayName("Like 요청 실패")
        class LikeFail {
            @Test
            @DisplayName("없는 게시글 좋아요")
            @WithUserDetails
            void likeArticleNotExist() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willReturn(testUser);
                willThrow(ArticleNotFoundException.class)
                        .given(articleService)
                        .likeArticle(1L, testUser);

                mvc.perform(post("/articles/1/like")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(404));

                verify(articleService).likeArticle(1L ,testUser);
            }

            @Test
            @DisplayName("로그인 안함")
            @WithUserDetails
            void likeArticleNoLogin() throws Exception {
                given(userService.userFromUserDetails(any(UserDetails.class)))
                        .willThrow(UnauthenticatedException.class);

                mvc.perform(post("/articles/1/like")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.successYn")
                                .value(false))
                        .andExpect(jsonPath("$.statusCode")
                                .value(401));

                verify(articleService,never()).likeArticle(any() ,any());
            }
        }
    }
}