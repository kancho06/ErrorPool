package com.sparta.errorpool.comment;

import com.sparta.errorpool.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
