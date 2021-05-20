package com.br.framework.model;

import com.br.framework.dto.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String text;
    private String image;
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_custom_id")
    private UserCustom userCustom;

    @ManyToMany(mappedBy = "posts")
    private Set<Comment> comments = new HashSet<>();

    public Post(PostDTO postDTO, UserCustom userCustom) {
        this.text = postDTO.getText();
        this.image = postDTO.getImage();
        this.link = postDTO.getLink();
        this.userCustom = userCustom;
    }
}
