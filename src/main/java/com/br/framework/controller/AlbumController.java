package com.br.framework.controller;

import com.br.framework.dto.AlbumDTO;
import com.br.framework.dto.PhotoDTO;
import com.br.framework.model.Album;
import com.br.framework.model.Photo;
import com.br.framework.model.UserCustom;
import com.br.framework.repository.AlbumRepository;
import com.br.framework.repository.PhotoRepository;
import com.br.framework.repository.UserRepository;
import com.br.framework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/albums")
    public ResponseEntity<Object> getAllAlbumByUser() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String username = jwtUtil.extractUsername(request.getHeader("Authorization"));

        Optional<Set<Album>> albumSet = albumRepository.findAlbumsByUserCustom(userRepository.findByUsername(username).get());

        if (!albumSet.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", "Não foi encontrado nenhum album cadastrado em seu nome"));
        }

        return ResponseEntity.ok(Collections.singletonMap("Albums", albumSet.get()));
    }

    @PostMapping("/new-album")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> saveNewAlbum(@RequestBody AlbumDTO albumDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();

        Album album = new Album(albumDTO, userCustom);
        albumRepository.saveAndFlush(album);

        Set<Photo> photos = new HashSet<>();

        for (PhotoDTO photoDTO: albumDTO.getPhotoDTOSet()) {
            Photo photo = new Photo(photoDTO, userCustom);
            photo.getAlbums().add(album);
            photos.add(photo);
        }
        photoRepository.saveAll(photos);
        photoRepository.flush();

        album.getPhotos().addAll(photos);
        albumRepository.saveAndFlush(album);

        return ResponseEntity.ok(Collections.singletonMap("Mensagem", "Album e fotos salvas com sucesso!"));
    }

    @PutMapping("/new-photo/{id}")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> saveNewPhotos(@RequestBody AlbumDTO albumDTO, @PathVariable String id) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();

        Optional<Album> album = albumRepository.findById(Long.valueOf(id));

        if (!album.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", String.format("Não foi encontrado o album com o id (%s) informado", id)));
        }

        Set<Photo> photos = new HashSet<>();

        for (PhotoDTO photoDTO: albumDTO.getPhotoDTOSet()) {
            Photo photo = new Photo(photoDTO, userCustom);
            photo.getAlbums().add(album.get());
            photos.add(photo);
        }
        photoRepository.saveAll(photos);
        photoRepository.flush();

        album.get().getPhotos().addAll(photos);
        albumRepository.saveAndFlush(album.get());

        return ResponseEntity.ok(Collections.singletonMap("Mensagem", "Foram adicionadas as fotos com sucesso no album"));
    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity<Object> deleteAlbum(@RequestBody AlbumDTO albumDTO, @PathVariable String id) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserCustom userCustom = userRepository.findByUsername(jwtUtil.extractUsername(request.getHeader("Authorization"))).get();

        Optional<Album> album = albumRepository.findById(Long.valueOf(id));

        if (!album.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Mensagem", String.format("Album não encontrado pelo id (%s) informado!", id)));
        }

        if (!album.get().getUserCustom().getUsername().equals(userCustom.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("Mensagem", "Album não pertence ao usuário logado, portanto a exclusão não é permitida!"));
        }

        albumRepository.delete(album.get());

        return ResponseEntity.ok().body(Collections.singletonMap("Mensagem", "Album excluído com sucesso"));
    }
}
