package com.sparta.errorpool.user;

import com.sparta.errorpool.article.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findBySkillId (Integer skillId);

    Optional<User> findBySocialId(Long socialId);

    Optional<User> findByEmail(String email);


}