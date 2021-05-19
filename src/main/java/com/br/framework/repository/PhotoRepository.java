package com.br.framework.repository;

import com.br.framework.model.Photo;
import com.br.framework.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByUserCustom(UserCustom userCustom);
}
