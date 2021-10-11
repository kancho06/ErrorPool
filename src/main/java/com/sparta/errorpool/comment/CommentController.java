package com.sparta.errorpool.comment;

import com.sparta.errorpool.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    
    // 댓글 추가
    @PostMapping("/comments")
    public void addComment(@RequestParam Long articleId, @RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.addComment(articleId, commentDto, userDetails.getUser());
    }

    // 댓글 수정
    @PostMapping("/comments/{articleId}")
    public void modifyComment(@PathVariable Long articleId, @RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.modifyComment(articleId, commentDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{articleId}")
    public void deleteComment(@PathVariable Long articleId, @RequestParam Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(articleId, commentId, userDetails.getUser());
    }
}
