package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleRepository;
import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.exception.CommentNotFoundException;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    public final CommentRepository commentRepository;
    public final ArticleRepository articleRepository;

    //댓글 추가
    public ResponseEntity addComment(Long articleId, CommentDto commentDto, User user) {

        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("해당 게시글을 찾을 수 없어 댓글을 추가할 수 없습니다."));

        Comment comment = new Comment(user, article, commentDto.getContent());

        //댓글 추가
        commentRepository.save(comment).getId();
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "댓글 추가가 완료되었습니다.", null), HttpStatus.OK);
    }

    //댓글 수정
    public ResponseEntity modifyComment(Long commentId, CommentDto commentDto, User user) {

        Long articleId = commentDto.getArticleId();

        // 화면에서 들어온 commentID를 이용하여 DB에 있는 articleID로 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("해당 게시글을 찾을 수 없어 댓글을 수정할 수 없습니다."));

        // 댓글 존재여부 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("해당 댓글을 찾을 수 없어 수정할 수 없습니다."));

        // DB에 있는 comment 의 articleId와 DB에 있는 article ID를 비교
        if(!article.getId().equals(comment.getArticle().getId()))
            new ArticleNotFoundException("해당 게시글을 또는 댓글의 정보가 잘못되었습니다. 관리자 확인이 필요합니다.");

        //댓글에 저장되어있는 사용자의 username과 현재 사용자의 username 비교하기
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new AccessDeniedException("회원님의 댓글만 수정할 수 있습니다.");

        // 댓글 update
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "댓글 수정이 완료되었습니다.", null), HttpStatus.OK);
    }

    //댓글 삭제
    public ResponseEntity deleteComment(Long articleId, Long commentId,User user) {

        // 게시글 존재여부 확인
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("해당 게시물을 찾을 수 없어 댓글을 삭제 할 수 없습니다."));
        
        // 댓글 존재여부 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("해당 댓글을 찾을 수 없어 삭제할 수 없습니다."));

        // DB에 있는 comment 의 articleId와 DB에 있는 article ID를 비교
        if(!article.getId().equals(comment.getArticle().getId()))
            new ArticleNotFoundException("해당 게시글을 또는 댓글의 정보가 잘못되었습니다. 관리자 확인이 필요합니다.");
        

        //댓글에 저장되어있는 사용자의 username과 현재 사용자의 username 비교하기
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new AccessDeniedException("회원님의 댓글만 삭제 할 수 있습니다.");

        commentRepository.delete(comment);
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "댓글 삭제가 완료되었습니다.", null), HttpStatus.OK);
    }

    // 마이 페이지 댓글 가져오기 
    public Page<Comment> getComments(User user) {
        return commentRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0,5));
    }
}
