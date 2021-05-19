package com.br.framework.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_custom_id")
    private UserCustom userCustom;

    private String image;

    @ManyToMany
    private Set<Album> albums;
}
