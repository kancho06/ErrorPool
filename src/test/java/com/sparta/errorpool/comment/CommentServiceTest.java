package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private
    CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("댓글 추가 성공")
    void addComment_Normal() {
        Long articleId = 30L;
        User user = userRepository.findById(1L).orElse(null);
        Article article = articleRepository.findById(31L).orElse(null);
        List<Comment> commentList = commentRepository.findAll();
        CommentDto commentDto = CommentDto.builder()
                .articleId(30L)
                .commentId(1L)
                .username("namelim@gmail.com")
                .content("댓글1")
                .build();

        Comment comment = new Comment(user, article,commentDto.getContent());
        commentService.addComment(articleId,commentDto, user);
    }


    @Test
    @DisplayName("댓글 추가 에러-게시글NULL")
    void addCommentError_NotFoundArticle() {
        User user = userRepository.findById(4L).orElse(null);

        Long articleId = 300L;

        Article[] articles = new Article[1];
        Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
            articles[0] = articleRepository.findById(articleId).orElseThrow(
                    () -> new ArticleNotFoundException("해당 게시글을 찾을 수 없어 댓글을 수정할 수 없습니다."));
        });

        CommentDto commentDto = CommentDto.builder()
                .articleId(30L)
                .commentId(1L)
                .username("namelim@gmail.com")
                .content("댓글1")
                .build();

        Comment comment = new Comment(user, articles[0], commentDto.getContent());
        commentService.addComment(articleId,commentDto, user);
    }

    @Test
    @DisplayName("댓글 추가 에러-DB사용자와 CommentDto사용자가 다를경우")
    void addCommentError_NotEqualDBUserCommentDtoUser() {

    }

    @Test
    @DisplayName("댓글 추가 에러-사용자NULL")
    void addCommentError_NotFoundUser() {

    }

    @Test
    void modifyComment() {

    }

    @Test
    void deleteComment() {
    }

    @Test
    void getComments() {
    }
}