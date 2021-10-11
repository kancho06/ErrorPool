package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
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
                () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
        );

        Comment comment = new Comment(user, article, commentDto.getContent());


        //댓글 추가
        commentRepository.save(comment).getId();
    }

    //댓글 수정
    public void modifyComment(Long articleId, CommentDto commentDto, User user) {

        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
        );

        // 댓글 존재여부 확인
        Comment comment = commentRepository.findById(commentDto.getArticleId()).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        //댓글에 저장되어있는 사용자의 username과 현재 사용자의 username 비교하기
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("회원님의 댓글이 아닙니다.");

        // 댓글 update
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

    
    //댓글 삭제
    public void deleteComment(Long articleId, Long commentId, User user) {
        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
        );

        // 댓글 존재여부 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        //댓글에 저장되어있는 사용자의 username과 현재 사용자의 username 비교하기
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("회원님의 댓글이 아닙니다.");

        commentRepository.delete(comment);
    }
}
