package com.br.framework.repository;

import com.br.framework.model.Album;
import com.br.framework.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByName(String name);
    Optional<Set<Album>> findAlbumsByUserCustom(UserCustom userCustom);
}
