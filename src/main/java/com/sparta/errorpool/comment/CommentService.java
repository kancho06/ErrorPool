package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    public final CommentRepository commentRepository;

    public Long addComment(Long articleId, CommentDto commentDto, User user) {

//        Comment comment = new Comment(user, article, commentDto.getContent());
//        commentRepository.save(comment);
//        return 1L;

    }
}
