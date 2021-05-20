package com.br.framework.model;

import com.br.framework.dto.AlbumDTO;
import com.br.framework.dto.PhotoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "album")
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "album_photo",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private Set<Photo> photos = new HashSet<>();

    @ManyToOne
    private UserCustom userCustom;

    public Album(AlbumDTO albumDTO, UserCustom userCustom) {
        this.name = albumDTO.getName();
        this.userCustom = userCustom;
    }
}
