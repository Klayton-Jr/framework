package com.br.framework.model;

import com.br.framework.dto.PhotoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "photo")
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_custom_id")
    private UserCustom userCustom;

    private String image;

    @ManyToMany(mappedBy = "photos")
    private Set<Album> albums = new HashSet<>();

    public Photo(PhotoDTO photoDTO, UserCustom userCustom) {
        this.image = photoDTO.getImage();
        this.userCustom = userCustom;
    }
}
