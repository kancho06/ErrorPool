package com.sparta.errorpool.mypage;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleService;
import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.comment.CommentService;
import com.sparta.errorpool.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = "MyPage Controller Api V1")
@RequestMapping("/mypage")
public class MyPageController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @ApiOperation(value = "내 게시글 조회")
    @GetMapping("/article")
    public List<Article> getArticles(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.getArticles(userDetails.getUser());
    }

    @ApiOperation(value = "내 댓글 조회")
    @GetMapping("/comment")
    public List<Comment> getComments(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.getComments(userDetails.getUser());
    }
}
