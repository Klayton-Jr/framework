package com.br.framework.controller;

import com.br.framework.model.UserCustom;
import com.br.framework.dto.UserDTO;
import com.br.framework.repository.UserRepository;
import com.br.framework.util.JwtUtil;
import com.br.framework.util.Util;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authenticate")
public class AutenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/allUsers")
    public ResponseEntity<Object> hello() {
        List<UserCustom> userCustoms = userRepository.findAll();

        return ResponseEntity.ok(userCustoms);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Mensagem", "Deve ser preenchido o usuário e a senha!"));
        }
        if (Util.notNull(userDTO.getUsername()).isEmpty() || Util.notNull(userDTO.getPassword()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Mensagem", "Usuário ou senha em branco, favor prencher."));
        }

        Optional<UserCustom> userCustomOptional = userRepository.findByUsername(userDTO.getUsername());

        if (userCustomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Mensagem", "Usuário já cadastrado!"));
        }
        String passwordEncrypted = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        userRepository.save(new UserCustom(userDTO.getUsername(), passwordEncrypted));

        return ResponseEntity.ok().body(Collections.singletonMap("Mensagem", "Usuário cadastrado com sucesso!"));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody UserDTO userDTO) {
        Optional<UserCustom> userCustomOptional = userRepository.findByUsername(userDTO.getUsername());

        if (!userCustomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", "Usuario não cadastrado, por favor faço o cadastro!"));
        }

        String hashed = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());

        if (!BCrypt.checkpw(userDTO.getPassword(), userCustomOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("Mensagem", "Senha não confere com usuario informado!"));
        }

        return ResponseEntity.ok(Collections.singletonMap("Token", "Bearer "+jwtUtil.generateToken(userDTO)));
    }
}
