package com.br.framework.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    private String image;
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_custom_id")
    private UserCustom userCustom;

    @ManyToMany(mappedBy = "posts")
    private Set<Comment> comments;
}
