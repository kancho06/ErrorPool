package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.ArticleCreateRequestDto;
import com.sparta.errorpool.article.dto.ArticleUpdateRequestDto;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRoleEnum;
import com.sparta.errorpool.util.ImageService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private LikeInfoRepository likeInfoRepository;

    private List<Article> mockArticleList = new ArrayList<>();
    User testUser;

    @BeforeEach
    private void beforeEach() {

        testUser = new User("rockintuna@naver.com", "password", "rockintuna", UserRoleEnum.USER, Skill.SPRING);
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
    @DisplayName("게시글 조회")
    class ReadArticle {
        @Nested
        @DisplayName("게시글 조회 성공")
        class ReadArticleSuccess {
            @Test
            void getArticlesInSkillAndCategory() {
                int page = 1;
                Skill skill = Skill.SPRING;
                Category category = Category.FREE_BOARD;
                given(articleRepository.findAllBySkillAndCategory(PageRequest.of(page-1, 6),skill, category))
                        .willReturn(new PageImpl<>(mockArticleList));

                Page<Article> articlePage =
                        articleService.getArticlesInSkillAndCategory(page,skill.getNum(),category.getNum());

                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(articlePage.getTotalElements()).isEqualTo(mockArticleList.size());
                softly.assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo(mockArticleList.get(0).getTitle());
                softly.assertThat(articlePage.getContent().get(0).getUser()).isEqualTo(mockArticleList.get(0).getUser());
                softly.assertThat(articlePage.getContent().get(0).getContent()).isEqualTo(mockArticleList.get(0).getContent());
                softly.assertThat(articlePage.getContent().get(3).getTitle()).isEqualTo(mockArticleList.get(3).getTitle());
                softly.assertThat(articlePage.getContent().get(3).getUser()).isEqualTo(mockArticleList.get(3).getUser());
                softly.assertThat(articlePage.getContent().get(3).getContent()).isEqualTo(mockArticleList.get(3).getContent());
                softly.assertAll();

                verify(articleRepository).findAllBySkillAndCategory(PageRequest.of(page-1, 6),skill, category);
            }

            @Test
            void getArticleAndUpViewCountById() {
                Long articleId = 1L;
                given(articleRepository.findById(articleId))
                        .willReturn(Optional.of(mockArticleList.get(0)));
                int viewCountBefor = mockArticleList.get(0).getViewCount();
                Article mockArticle = articleService.getArticleAndUpViewCountById(articleId);

                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(mockArticle.getTitle()).isEqualTo(mockArticleList.get(0).getTitle());
                softly.assertThat(mockArticle.getUser()).isEqualTo(mockArticleList.get(0).getUser());
                softly.assertThat(mockArticle.getContent()).isEqualTo(mockArticleList.get(0).getContent());
                softly.assertThat(mockArticle.getViewCount()).isEqualTo(viewCountBefor+1);
                softly.assertAll();

                verify(articleRepository).findById(articleId);
            }

            @Test
            void getBestArticleListOfAllSkill() {
                given(articleRepository.findTopByOrderByLikeCountDesc(PageRequest.of(0,5)))
                        .willReturn(new PageImpl<>(mockArticleList));
                Page<Article> articlePage = articleService.getBestArticleListOfAllSkill();

                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(articlePage.getTotalElements()).isEqualTo(mockArticleList.size());
                softly.assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo(mockArticleList.get(0).getTitle());
                softly.assertThat(articlePage.getContent().get(0).getUser()).isEqualTo(mockArticleList.get(0).getUser());
                softly.assertThat(articlePage.getContent().get(0).getContent()).isEqualTo(mockArticleList.get(0).getContent());
                softly.assertThat(articlePage.getContent().get(3).getTitle()).isEqualTo(mockArticleList.get(3).getTitle());
                softly.assertThat(articlePage.getContent().get(3).getUser()).isEqualTo(mockArticleList.get(3).getUser());
                softly.assertThat(articlePage.getContent().get(3).getContent()).isEqualTo(mockArticleList.get(3).getContent());
                softly.assertAll();

                verify(articleRepository).findTopByOrderByLikeCountDesc(PageRequest.of(0,5));
            }

            @Test
            void getBestArticleListIn() {
                Skill skill = Skill.SPRING;
                given(articleRepository.findTopBySkillOrderByLikeCountDesc(PageRequest.of(0,5), skill))
                        .willReturn(new PageImpl<>(mockArticleList));
                Page<Article> articlePage = articleService.getBestArticleListIn(skill);

                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(articlePage.getTotalElements()).isEqualTo(mockArticleList.size());
                softly.assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo(mockArticleList.get(0).getTitle());
                softly.assertThat(articlePage.getContent().get(0).getUser()).isEqualTo(mockArticleList.get(0).getUser());
                softly.assertThat(articlePage.getContent().get(0).getContent()).isEqualTo(mockArticleList.get(0).getContent());
                softly.assertThat(articlePage.getContent().get(3).getTitle()).isEqualTo(mockArticleList.get(3).getTitle());
                softly.assertThat(articlePage.getContent().get(3).getUser()).isEqualTo(mockArticleList.get(3).getUser());
                softly.assertThat(articlePage.getContent().get(3).getContent()).isEqualTo(mockArticleList.get(3).getContent());
                softly.assertAll();

                verify(articleRepository).findTopBySkillOrderByLikeCountDesc(PageRequest.of(0,5), skill);
            }

            @Test
            void getArticles() {
                given(articleRepository.findAllByUserOrderByCreatedAtDesc(testUser, PageRequest.of(0,5)))
                        .willReturn(new PageImpl<>(mockArticleList));
                Page<Article> articlePage = articleService.getArticles(testUser);

                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(articlePage.getTotalElements()).isEqualTo(mockArticleList.size());
                softly.assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo(mockArticleList.get(0).getTitle());
                softly.assertThat(articlePage.getContent().get(0).getUser()).isEqualTo(mockArticleList.get(0).getUser());
                softly.assertThat(articlePage.getContent().get(0).getContent()).isEqualTo(mockArticleList.get(0).getContent());
                softly.assertThat(articlePage.getContent().get(3).getTitle()).isEqualTo(mockArticleList.get(3).getTitle());
                softly.assertThat(articlePage.getContent().get(3).getUser()).isEqualTo(mockArticleList.get(3).getUser());
                softly.assertThat(articlePage.getContent().get(3).getContent()).isEqualTo(mockArticleList.get(3).getContent());
                softly.assertAll();

                verify(articleRepository).findAllByUserOrderByCreatedAtDesc(testUser, PageRequest.of(0,5));
            }
        }
        @Nested
        @DisplayName("게시글 조회 실패")
        class ReadArticleFail {
        }
    }
    @Nested
    @DisplayName("게시글 작성")
    class CreateArticle {
        @Nested
        @DisplayName("게시글 작성 성공")
        class CreateArticleSuccess {
            @Test
            @DisplayName("이미지 없이 작성하기")
            void createArticleWithNoImage() {
                ArticleCreateRequestDto requestDto =
                        new ArticleCreateRequestDto(
                                "test",
                                null,
                                "content",
                                1,
                                1
                        );
                articleService.createArticle(requestDto, testUser);

                verify(articleRepository).save(any(Article.class));
            }

            @Test
            @DisplayName("이미지 포함 작성하기")
            void createArticleWithImage() {
                given(imageService.saveFile(any(MockMultipartFile.class)))
                        .willReturn(Paths.get("test"));
                ArticleCreateRequestDto requestDto =
                        new ArticleCreateRequestDto(
                                "test",
                                new MockMultipartFile("mockImage", new byte[1]),
                                "content",
                                1,
                                1
                        );
                articleService.createArticle(requestDto, testUser);

                verify(articleRepository).save(any(Article.class));
            }
        }
        @Nested
        @DisplayName("게시글 작성 실패")
        class CreateArticleFail {
        }
    }
    @Nested
    @DisplayName("게시글 수정")
    class UpdateArticle {
        @Nested
        @DisplayName("게시글 수정 성공")
        class UpdateArticleSuccess {
            @Test
            void updateArticle() {
                given(imageService.saveFile(any(MockMultipartFile.class)))
                        .willReturn(Paths.get("test"));
                given(articleRepository.findById(1L)).willReturn(Optional.of(mockArticleList.get(0)));
                ArticleUpdateRequestDto requestDto =
                        new ArticleUpdateRequestDto(
                                "test",
                                new MockMultipartFile("mockImage", new byte[1]),
                                "content"
                        );

                articleService.updateArticle(1L, requestDto, testUser);
                verify(articleRepository).save(any(Article.class));
            }
        }
        @Nested
        @DisplayName("게시글 수정 실패")
        class UpdateArticleFail {
            @Test
            @DisplayName("없는 게시글 수정")
            void updateArticleNoExist() {
                ArticleUpdateRequestDto requestDto =
                        new ArticleUpdateRequestDto(
                                "test",
                                new MockMultipartFile("mockImage", new byte[1]),
                                "content"
                        );

                given(articleRepository.findById(1L)).willReturn(Optional.empty());

                assertThrows(ArticleNotFoundException.class,
                        () -> articleService.updateArticle(1L, requestDto, testUser));
                verify(articleRepository, never()).save(any(Article.class));
            }
        }
    }
    @Nested
    @DisplayName("게시글 삭제")
    class DeleteArticle {
        @Nested
        @DisplayName("게시글 삭제 성공")
        class DeleteArticleSuccess {
            @Test
            void deleteArticle() {
                given(articleRepository.findById(1L)).willReturn(Optional.of(mockArticleList.get(0)));

                articleService.deleteArticle(1L, testUser);
                verify(articleRepository).delete(any(Article.class));
            }
        }
        @Nested
        @DisplayName("게시글 삭제 실패")
        class DeleteArticleFail {
            @Test
            @DisplayName("없는 게시글 삭제")
            void deleteArticleNoExist() {
                given(articleRepository.findById(1L)).willReturn(Optional.empty());


                assertThrows(ArticleNotFoundException.class,
                        () -> articleService.deleteArticle(1L, testUser));
                verify(articleRepository, never()).delete(any(Article.class));
            }
        }
    }

}