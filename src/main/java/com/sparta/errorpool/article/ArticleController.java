package com.sparta.errorpool.article;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles/skill/{skill_id}/{category_id}")
    public List<Article> getArticlesInSkillAndCategory(@PathVariable("skill_id") Integer skillId,
                                      @PathVariable("category_id") Integer categoryId) {
        return articleService.getArticlesInSkillAndCategory(skillId, categoryId);
    }
}
