package com.br.framework.repository;

import com.br.framework.model.Post;
import com.br.framework.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUserCustom(UserCustom userCustom);

    @Query("from Post post join fetch post.comments where post.id = :id")
    Post findWithComments(@Param("id") Long id);
}
