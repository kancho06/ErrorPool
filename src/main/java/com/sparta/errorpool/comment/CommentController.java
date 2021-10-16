package com.sparta.errorpool.comment;

import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RestController
@Api(tags = "Comment Controller Api V1")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @ApiOperation(value = "댓글 추가")
    @PostMapping("/comments")
    public ResponseEntity<DefaultResponse<Comment>> addComment(
            @RequestBody @ApiParam(value = "댓글 하나 정보를 갖는 객체", required = true) CommentDto commentDto
            ,@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails
    ) {
        User user = userService.userFromUserDetails(userDetails);
        Long articleId = commentDto.getArticleId();
        Comment comment = commentService.addComment(articleId, commentDto, user);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "댓글 추가가 완료되었습니다.", comment));
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping("/comments/{commentId}")
    public void modifyComment(
            @PathVariable @ApiParam(value = "댓글 아이디", required = true) Long commentId
            , @RequestBody @ApiParam(value = "댓글 한개의 정보를 가진 객체", required = true) CommentDto commentDto
            , @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails
    ) {
        commentService.modifyComment(commentId, commentDto, userDetails.getUser());
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(
            @PathVariable @ApiParam(value = "댓글 아이디", required = true) Long commentId
            , @RequestParam @ApiParam(value = "댓글 한개의 정보를 가진 객체", required = true) Long articleId
            , @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails
    ) {
        commentService.deleteComment(articleId, commentId, userDetails.getUser());
    }
}
