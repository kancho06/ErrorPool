package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.exception.CommentNotFoundException;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    public final CommentRepository commentRepository;
    public final ArticleRepository articleRepository;

    //댓글 추가
    public void addComment(Long articleId, CommentDto commentDto, User user) {

        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("해당 게시글을 찾을 수 없어 댓글 추가할 수 없습니다.")
        );

        Comment comment = new Comment(user, article, commentDto.getContent());

        //댓글 추가
        commentRepository.save(comment).getId();
    }

    //댓글 수정
    public void modifyComment(Long commentId, CommentDto commentDto, User user) {

        Long articleId = commentDto.getArticleId();
        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("해당 게시글을 찾을 수 없어 댓글을 수정할 수 없습니다.")
        );

        // 댓글 존재여부 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("해당 댓글이 존재하지 않아 수정할 수 없습니다.")
        );
        
        //댓글에 저장되어있는 사용자의 username과 현재 사용자의 username 비교하기
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new AccessDeniedException("회원님의 댓글만 수정이 가능합니다.");

        // 댓글 update
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

    
    //댓글 삭제
    public void deleteComment(Long articleId, Long commentId, User user) {
        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("해당 게시물을 찾을 수 없어 댓글을 삭제 할 수 없습니다.")
        );

        // 댓글 존재여부 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("해당 댓글이 존재하지 않아 삭제할 수 없습니다.")
        );

        //댓글에 저장되어있는 사용자의 username과 현재 사용자의 username 비교하기
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new AccessDeniedException("회원님의 댓글만 삭제가 가능합니다.");

        commentRepository.delete(comment);
    }
}
