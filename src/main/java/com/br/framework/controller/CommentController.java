package com.br.framework.controller;

import com.br.framework.dto.CommentDTO;
import com.br.framework.model.Comment;
import com.br.framework.model.Post;
import com.br.framework.model.UserCustom;
import com.br.framework.repository.CommentRepository;
import com.br.framework.repository.PostRepository;
import com.br.framework.repository.UserRepository;
import com.br.framework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/comment-new")
    public ResponseEntity<Object> postComment(@RequestBody CommentDTO commentDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();
        Optional<Post> post = postRepository.findById(commentDTO.getPostId());

        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", "Não foi encontrado o post a ser comentado!"));
        }

        Comment comment = new Comment(commentDTO, userCustom, post.get());

        commentRepository.save(comment);

        return ResponseEntity.ok().body(Collections.singletonMap("Mensagem", "Comentario salvo com sucesso no post"));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Object> deleteComment(@RequestBody CommentDTO commentDTO, @PathVariable String id) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();

        Optional<Comment> comment = commentRepository.findById(Long.valueOf(id));

        if (!comment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", "Comentario não encontrado!"));
        }
        if (!comment.get().getUserCustom().getUsername().equals(userCustom.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("Mensagem", "Comentario não pertence ao usuário logado, portanto a exclusão não é permitida!"));
        }

        commentRepository.delete(comment.get());

        return ResponseEntity.ok().body(Collections.singletonMap("Mensagem", "Comentario excluído com sucesso"));
    }

}
