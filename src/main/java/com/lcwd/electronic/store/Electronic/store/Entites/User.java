package com.lcwd.electronic.store.Electronic.store.Entites;

import com.lcwd.electronic.store.Electronic.store.Validate.ImageValidator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private  String UserId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email",unique = true)
    private  String email;
    @Column(length = 15)
    private String password;
    private String gender;
    @Column(length = 1000)
    private  String about;

    private String imageName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();
}
