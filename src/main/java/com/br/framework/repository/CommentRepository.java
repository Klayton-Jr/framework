package com.br.framework.repository;

import com.br.framework.model.Comment;
import com.br.framework.model.Post;
import com.br.framework.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByUserCustom(UserCustom userCustom);
    Optional<Set<Comment>> findCommentsByPostsIn(Set<Post> posts);
}
