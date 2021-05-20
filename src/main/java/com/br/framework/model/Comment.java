package com.br.framework.model;

import com.br.framework.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_custom_id")
    private UserCustom userCustom;

    private String text;

    @ManyToMany
    @JoinTable(
            name = "post_comment",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> posts = new HashSet<>();

    public Comment(CommentDTO commentDTO, UserCustom userCustom, Post post) {
        this.text = commentDTO.getText();
        this.userCustom = userCustom;
        this.posts.add(post);
    }
}
