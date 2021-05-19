package com.br.framework.repository;

import com.br.framework.model.Post;
import com.br.framework.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUserCustom(UserCustom userCustom);
}
