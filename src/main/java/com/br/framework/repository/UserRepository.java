package com.br.framework.repository;

import com.br.framework.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCustom, Long> {
    Optional<UserCustom> findByUsername(String username);
}