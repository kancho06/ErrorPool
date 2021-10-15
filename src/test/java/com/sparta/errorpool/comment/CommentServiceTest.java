package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.exception.CommentNotFoundException;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void addCommentNormal() {
        Long articleId = 30L;
        User user = userRepository.findById(1L).orElse(null);
        Article article = articleRepository.findById(31L).orElse(null);
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
    @DisplayName("댓글 추가 에러-> 게시글NULL")
    void addCommentError_NotFoundArticle() {
        User user = userRepository.findById(4L).orElse(null);

        Long articleId = 300L;

        CommentDto commentDto = CommentDto.builder()
                .articleId(30L)
                .commentId(1L)
                .username("namelim@gmail.com")
                .content("댓글1")
                .build();


        Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
            commentService.addComment(articleId,commentDto, user);
        });

        assertEquals("해당 게시글을 찾을 수 없어 댓글을 추가할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 추가 에러-> DB사용자와 CommentDto사용자가 다를경우")
    void addCommentError_NotEqualDBUserCommentDtoUser() {

    }

    @Test
    @DisplayName("댓글 추가 에러-> 사용자NULL")
    void addCommentError_NotFoundUser() {

    }

    @Test
    @DisplayName("댓글 수정 성공")
    void modifyCommentNormal() {
        Long commentId = 1L;
        User user = userRepository.findById(4L).orElse(null);
        CommentDto commentDto = CommentDto.builder()
                .articleId(21L)
                .commentId(commentId)
                .username("namelim@gmail.com")
                .content("[정상 테스트] 댓글 수정")
                .build();

        commentService.modifyComment(commentId, commentDto, user);
    }


    @Test
    @DisplayName("댓글 수정 에러-> 게시글 NULL")
    void modifyCommentError_NotFoundArticle() {

        Long commentId = 1L;
        Long articleId = 300L;
        User user = userRepository.findById(4L).orElse(null);
        CommentDto commentDto = CommentDto.builder()
                .articleId(articleId)
                .commentId(commentId)
                .username("namelim@gmail.com")
                .content("[에러 테스트] 댓글 수정-> 게시글 NULL")
                .build();

        Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
            commentService.modifyComment(commentId,commentDto, user);
        });

        assertEquals("해당 게시글을 찾을 수 없어 댓글을 수정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 에러-> 댓글 NULL")
    void modifyCommentError_NotFoundComment() {
        Long commentId = 400L;
        Long articleId = 21L;
        User user = userRepository.findById(4L).orElse(null);

        CommentDto commentDto = CommentDto.builder()
                .articleId(articleId)
                .commentId(commentId)
                .username("namelim@gmail.com")
                .content("[에러 테스트] 댓글 수정-> 게시글 NULL")
                .build();

        Exception exception = assertThrows(CommentNotFoundException.class, () -> {
            commentService.modifyComment(commentId,commentDto, user);
        });

        assertEquals("해당 댓글을 찾을 수 없어 수정할 수 없습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("댓글 수정 에러-> article DB,comment DB에서 게시글의 번호가 서로 다름")
    void modifyCommentError_NotEqualArticleDBAndCommentDB() {
        Long commentId = 1L;
        Long articleId = 30L;
        User user = userRepository.findById(4L).orElse(null);

        CommentDto commentDto = CommentDto.builder()
                .articleId(articleId)
                .commentId(commentId)
                .username("namelim@gmail.com")
                .content("[에러 테스트] 댓글 수정-> article DB,comment DB에서 게시글의 번호가 서로 다름")
                .build();

        Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
            commentService.modifyComment(commentId,commentDto, user);
        });

        assertEquals("해당 게시글을 또는 댓글의 정보가 잘못되었습니다. 관리자 확인이 필요합니다."
                , exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 에러-> 나의 댓글이 아닌 경우")
    void modifyCommentError_() {
        Long commentId = 1L;
        Long articleId = 30L;
        User user = userRepository.findById(4L).orElse(null);

        CommentDto commentDto = CommentDto.builder()
                .articleId(articleId)
                .commentId(commentId)
                .username("namelim@gmail.com")
                .content("[에러 테스트] 댓글 수정-> 내 댓글이 아닌경우")
                .build();

        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            commentService.modifyComment(commentId,commentDto, user);
        });

        assertEquals("회원님의 댓글만 수정할 수 있습니다."
                , exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentNormal() {
        Long commentId = 1L;
        Long articleId = 21L;
        User user = userRepository.findById(1L).orElse(null);
        CommentDto commentDto = CommentDto.builder()
                .articleId(21L)
                .commentId(commentId)
                .username("namelim@gmail.com")
                .content("[정상 테스트] 댓글 삭제")
                .build();

        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            commentService.deleteComment(articleId,commentId, user);
        });

        assertEquals("회원님의 댓글만 수정할 수 있습니다."
                , exception.getMessage());
    }

    @Test
    void getComments() {
    }
}