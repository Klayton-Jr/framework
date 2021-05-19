package com.br.framework.controller;

import com.br.framework.dto.PostDTO;
import com.br.framework.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/new")
    public ResponseEntity<String> newPost(@RequestBody PostDTO postDTO, UserDTO userDTO) {
        return null;
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removePost(@RequestBody PostDTO postDTO, UserDTO userDTO) {
        //somente remover se o usuario que esta solicitando é o dono do post
        return null;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getPost() {
        return null;
    }
}
