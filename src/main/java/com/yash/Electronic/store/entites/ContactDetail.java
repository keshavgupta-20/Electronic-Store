package com.yash.Electronic.store.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetail {
    @Id
    private String contactDetailId;
    private String houseNo;
    private String street;
    private String city;
    private String pincode;
    private String country;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String contactNo;

}
