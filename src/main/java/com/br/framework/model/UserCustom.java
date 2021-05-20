package com.br.framework.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "userCustom")
@NoArgsConstructor
public class UserCustom implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;

    public UserCustom(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @OneToMany(mappedBy = "userCustom")
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "userCustom")
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(mappedBy = "userCustom")
    private Set<Comment> comments = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
