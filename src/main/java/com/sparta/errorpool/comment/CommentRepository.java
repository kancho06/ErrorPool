package com.sparta.errorpool.comment;

import com.sparta.errorpool.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByUserOrderByCreatedAtDesc(User user);
    List<Comment> findAllByArticleId(Long article_id);
}
