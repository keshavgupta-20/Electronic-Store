package com.org.Electronic.store.entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    //ADMIN

    //Normal

    @Id
    @Column(name = "user_id")
    private  String userId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email",unique = true)
    private  String email;



    @Column(length = 500)
    private String password;
    private String gender;
    @Column(length = 1000)
    private  String about;

    private String imageName;

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       Set<SimpleGrantedAuthority> authoritySet =  roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return authoritySet;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
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
