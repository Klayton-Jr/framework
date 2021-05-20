package com.br.framework.controller;

import com.br.framework.dto.PostDTO;
import com.br.framework.dto.UserDTO;
import com.br.framework.model.Post;
import com.br.framework.model.UserCustom;
import com.br.framework.repository.PostRepository;
import com.br.framework.repository.UserRepository;
import com.br.framework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<Object> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", "Não foi encontrado nenhum post"));
        }

        return ResponseEntity.ok().body(posts);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPostById(@PathVariable String id) {
        Optional<Post> post = postRepository.findWithComments(Long.valueOf(id));

        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", String.format("Não foi encontrado nenhum post com o id (%s)", id)));
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> newPost(@RequestBody PostDTO postDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();

        Post post = new Post(postDTO, userCustom);

        postRepository.save(post);

        return ResponseEntity.ok(Collections.singletonMap("Mensagem", "Post cadastrado com sucesso"));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Object> removePost(@RequestBody PostDTO postDTO, @PathVariable String id) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();

        Optional<Post> post = postRepository.findById(Long.valueOf(id));

        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", String.format("Não foi encontrado nenhum post para o id (%s) informado", id)));
        }

        if (!post.get().getUserCustom().getUsername().equals(userCustom.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("Mensagem", "Post não pertence ao usuário logado, portanto a exclusão não é permitida!"));
        }

        postRepository.delete(post.get());

        return ResponseEntity.ok(Collections.singletonMap("Mensagem", "Post excluído com sucesso"));
    }

}
