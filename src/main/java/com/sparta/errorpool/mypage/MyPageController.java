package com.sparta.errorpool.mypage;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.ArticleService;
import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.comment.CommentService;
import com.sparta.errorpool.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/article")
    public Page<Article> getArticles(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.getArticles(userDetails.getUser());
    }

    @GetMapping("/comment")
    public Page<Comment> getComments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.getComments(userDetails.getUser());
    }
}
