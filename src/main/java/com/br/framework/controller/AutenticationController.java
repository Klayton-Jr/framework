package com.br.framework.controller;

import com.br.framework.model.UserCustom;
import com.br.framework.dto.UserDTO;
import com.br.framework.repository.UserRepository;
import com.br.framework.util.JwtUtil;
import com.br.framework.util.Util;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/authenticate")
public class AutenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson("Deve ser preenchido o usuário e a senha!"));
        }
        if (Util.notNull(userDTO.getUsername()).isEmpty() || Util.notNull(userDTO.getPassword()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson("Usuário ou senha em branco, favor prencher."));
        }

        Optional<UserCustom> userCustomOptional = userRepository.findByUsername(userDTO.getUsername());

        if (userCustomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson("Usuário já cadastrado!"));
        }

        userRepository.save(new UserCustom(userDTO.getUsername(), userDTO.getPassword()));

        return ResponseEntity.ok().body(new Gson().toJson("Usuário cadastrado com sucesso!"));
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        Optional<UserCustom> userCustomOptional = userRepository.findByUsername(userDTO.getUsername());

        if (!userCustomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não cadastrado, por favor faço o cadastro!");
        }

        String hashed = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());

        if (!BCrypt.checkpw(userDTO.getPassword(), userCustomOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Senha não confere com usuario informado!");
        }

        return ResponseEntity.ok(new Gson().toJson(("Token: " + "Bearer "+jwtUtil.generateToken(userDTO)).toString()));
    }
}
